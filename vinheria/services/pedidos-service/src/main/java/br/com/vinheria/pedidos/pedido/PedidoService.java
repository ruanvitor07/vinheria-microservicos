package br.com.vinheria.pedidos.pedido;

import br.com.vinheria.pedidos.estoque.EstoqueClient;
import br.com.vinheria.pedidos.estoque.EstoqueIndisponivelException;
import br.com.vinheria.pedidos.estoque.EstoqueRecusouException;
import br.com.vinheria.pedidos.estoque.ReservaResponse;
import br.com.vinheria.pedidos.messaging.EventoPublisher;
import br.com.vinheria.pedidos.messaging.Eventos;
import br.com.vinheria.pedidos.pedido.dto.CriarPedidoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

/**
 * Orquestra a Saga do pedido (versão simples):
 *   1) salva o pedido como PENDENTE
 *   2) pede ao Estoque, via REST, a reserva dos itens
 *   3) sucesso -> CONFIRMADO + evento pedido.confirmado | falha -> RECUSADO
 *   Cancelamento (compensação): libera a reserva no Estoque e marca CANCELADO.
 *
 * Os métodos NÃO são @Transactional de propósito: não queremos manter uma conexão/transação
 * de banco aberta enquanto esperamos uma chamada HTTP a outro serviço.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final EstoqueClient estoqueClient;
    private final EventoPublisher eventoPublisher;

    public Pedido criar(CriarPedidoRequest request, String clienteUsername, String token) {
        List<ItemPedido> itens = request.itens().stream()
                .map(i -> new ItemPedido(i.sku(), i.quantidade(), null)).toList();
        Pedido pedido = pedidoRepository.save(new Pedido(clienteUsername, itens));

        try {
            ReservaResponse reserva = estoqueClient.reservar(pedido.getId(), request.itens(), token);
            List<ItemPedido> itensComPreco = reserva.itens().stream()
                    .map(i -> new ItemPedido(i.sku(), i.quantidade(), i.precoUnitario())).toList();
            pedido.confirmar(reserva.valorTotal(), itensComPreco);
            pedidoRepository.save(pedido);

            eventoPublisher.publicar(Eventos.PEDIDO_CONFIRMADO,
                    new Eventos.PedidoConfirmado(pedido.getId(), clienteUsername, pedido.getValorTotal(), Instant.now()));
        } catch (EstoqueRecusouException e) {
            pedido.recusar(e.getMessage());
            pedidoRepository.save(pedido);
        } catch (EstoqueIndisponivelException e) {
            log.warn("Estoque indisponível ao processar o pedido {}", pedido.getId(), e);
            pedido.recusar("Serviço de estoque indisponível no momento. Tente novamente.");
            pedidoRepository.save(pedido);
        }
        return pedido;
    }

    public Pedido cancelar(Long id, String token) {
        Pedido pedido = buscar(id);
        if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só é possível cancelar pedidos CONFIRMADOS (status atual: " + pedido.getStatus() + ")");
        }
        try {
            estoqueClient.liberarReserva(id, token);   // compensação
        } catch (EstoqueRecusouException | EstoqueIndisponivelException e) {
            // Se não conseguimos liberar o estoque, NÃO cancelamos: evita inconsistência.
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Não foi possível liberar a reserva no estoque: " + e.getMessage());
        }
        pedido.cancelar();
        pedidoRepository.save(pedido);
        eventoPublisher.publicar(Eventos.PEDIDO_CANCELADO,
                new Eventos.PedidoCancelado(pedido.getId(), pedido.getClienteUsername(), Instant.now()));
        return pedido;
    }

    public Pedido buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + id));
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarDoCliente(String clienteUsername) {
        return pedidoRepository.findByClienteUsernameOrderByIdDesc(clienteUsername);
    }
}
