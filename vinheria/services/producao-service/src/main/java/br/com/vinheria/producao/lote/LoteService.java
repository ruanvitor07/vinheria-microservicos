package br.com.vinheria.producao.lote;

import br.com.vinheria.producao.lote.dto.CriarLoteRequest;
import br.com.vinheria.producao.messaging.EventoPublisher;
import br.com.vinheria.producao.messaging.Eventos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final EventoPublisher eventoPublisher;

    @Transactional
    public Lote criar(CriarLoteRequest req) {
        if (loteRepository.existsByCodigo(req.codigo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um lote com o código " + req.codigo());
        }
        Lote lote = loteRepository.save(new Lote(req.codigo(), req.uva(), req.safra(), req.fornecedorUva(),
                req.quantidadeKg(), req.sku(), req.nomeProduto()));

        eventoPublisher.publicar(Eventos.LOTE_CRIADO, new Eventos.LoteCriado(
                lote.getCodigo(), lote.getUva(), lote.getSafra(), lote.getFornecedorUva(),
                lote.getQuantidadeKg(), Instant.now()));
        return lote;
    }

    @Transactional(readOnly = true)
    public List<Lote> listar() {
        return loteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Lote buscarPorCodigo(String codigo) {
        return loteRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado: " + codigo));
    }

    @Transactional
    public Lote concluirEtapaAtual(String codigo, Integer quantidadeGarrafas) {
        Lote lote = buscarPorCodigo(codigo);
        Etapa etapaConcluida = lote.getEtapaAtual();

        try {
            lote.concluirEtapa(quantidadeGarrafas);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        eventoPublisher.publicar(Eventos.LOTE_ETAPA_CONCLUIDA,
                new Eventos.EtapaConcluida(codigo, etapaConcluida.name(), Instant.now()));

        if (lote.getStatus() == StatusLote.FINALIZADO) {
            // O Estoque ouve este evento e dá entrada das garrafas — sem a Produção conhecer o Estoque.
            eventoPublisher.publicar(Eventos.LOTE_ENGARRAFADO, new Eventos.LoteEngarrafado(
                    codigo, lote.getSku(), lote.getNomeProduto(), lote.getQuantidadeGarrafas(), Instant.now()));
        }
        return lote;
    }

    @Transactional
    public void colocarEmQuarentena(String codigo) {
        buscarPorCodigo(codigo).colocarEmQuarentena();
    }

    @Transactional
    public void liberarDaQuarentena(String codigo) {
        buscarPorCodigo(codigo).liberarDaQuarentena();
    }
}
