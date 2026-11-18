package br.com.vinheria.estoque.reserva;

import br.com.vinheria.estoque.messaging.EventoPublisher;
import br.com.vinheria.estoque.messaging.Eventos;
import br.com.vinheria.estoque.produto.*;
import br.com.vinheria.estoque.reserva.dto.ReservaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final ProdutoRepository produtoRepository;
    private final ReservaRepository reservaRepository;
    private final EntradaEstoqueRepository entradaRepository;
    private final EventoPublisher eventoPublisher;

    /**
     * Reserva todos os itens ou nenhum (tudo na mesma transação).
     * Chamada de forma SÍNCRONA (REST) pelo serviço de Pedidos, que precisa da resposta para continuar.
     */
    @Transactional
    public Reserva reservar(ReservaRequest req) {
        var existente = reservaRepository.findByPedidoId(req.pedidoId());
        if (existente.isPresent()) {
            if (existente.get().getStatus() == StatusReserva.ATIVA) {
                return existente.get(); // repetição da mesma chamada: devolve a reserva já feita
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A reserva do pedido " + req.pedidoId() + " foi cancelada");
        }

        // soma quantidades caso o mesmo SKU venha em mais de uma linha
        Map<String, Integer> quantidadePorSku = new TreeMap<>();
        req.itens().forEach(i -> quantidadePorSku.merge(i.sku(), i.quantidade(), Integer::sum));

        Map<String, Produto> produtos = produtoRepository.buscarParaAtualizar(quantidadePorSku.keySet()).stream()
                .collect(Collectors.toMap(Produto::getSku, Function.identity()));

        Reserva reserva = new Reserva(req.pedidoId());
        for (var entrada : quantidadePorSku.entrySet()) {
            Produto produto = produtos.get(entrada.getKey());
            if (produto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + entrada.getKey());
            }
            try {
                produto.reservar(entrada.getValue());
            } catch (IllegalStateException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()); // faz rollback de tudo
            }
            reserva.adicionarItem(produto.getSku(), entrada.getValue(), produto.getPreco());
        }
        reservaRepository.save(reserva);

        // Alerta de estoque baixo: assíncrono, ninguém precisa esperar por isso.
        for (Produto produto : produtos.values()) {
            if (produto.estoqueBaixo()) {
                eventoPublisher.publicar(Eventos.ESTOQUE_BAIXO, new Eventos.EstoqueBaixo(
                        produto.getSku(), produto.getNome(), produto.disponivel(), produto.getEstoqueMinimo(), Instant.now()));
            }
        }
        return reserva;
    }

    /** Compensação da Saga: libera a reserva de um pedido cancelado. Idempotente. */
    @Transactional
    public void liberarReserva(Long pedidoId) {
        Reserva reserva = reservaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada para o pedido " + pedidoId));
        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            return;
        }
        List<String> skus = reserva.getItens().stream().map(ItemReserva::getSku).distinct().toList();
        Map<String, Produto> produtos = produtoRepository.buscarParaAtualizar(skus).stream()
                .collect(Collectors.toMap(Produto::getSku, Function.identity()));
        reserva.getItens().forEach(item -> produtos.get(item.getSku()).liberar(item.getQuantidade()));
        reserva.cancelar();
    }

    /** Chamado pelo listener de lote.engarrafado. Idempotente graças ao loteCodigo único. */
    @Transactional
    public void registrarEntradaDeProducao(Eventos.LoteEngarrafado evento) {
        if (entradaRepository.existsByLoteCodigo(evento.loteCodigo())) {
            log.info("Entrada do lote {} já registrada; evento duplicado ignorado", evento.loteCodigo());
            return;
        }
        // Se o SKU não existir, lança exceção: após os retries, a mensagem vai para a DLQ
        // e um administrador pode cadastrar o produto e reprocessá-la.
        Produto produto = produtoRepository.buscarParaAtualizar(List.of(evento.sku())).stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("SKU não cadastrado no estoque: " + evento.sku()));

        produto.registrarEntrada(evento.quantidadeGarrafas());
        entradaRepository.save(new EntradaEstoque(evento.loteCodigo(), evento.sku(), evento.quantidadeGarrafas()));
        log.info("Entrada de {} garrafas de {} (lote {})", evento.quantidadeGarrafas(), evento.sku(), evento.loteCodigo());
    }
}
