package br.com.vinheria.qualidade.analise;

import br.com.vinheria.qualidade.analise.dto.RegistrarAnaliseRequest;
import br.com.vinheria.qualidade.messaging.EventoPublisher;
import br.com.vinheria.qualidade.messaging.Eventos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnaliseService {

    private final AnaliseRepository analiseRepository;
    private final LoteConhecidoRepository loteConhecidoRepository;
    private final EventoPublisher eventoPublisher;

    @Transactional
    public Analise registrar(RegistrarAnaliseRequest req, String analista) {
        if (!loteConhecidoRepository.existsById(req.loteCodigo())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Lote " + req.loteCodigo() + " não encontrado (ainda não chegou o evento lote.criado?)");
        }

        List<String> falhas = RegrasQualidade.verificar(
                req.ph(), req.acidezTotal(), req.teorAlcoolico(), req.temperaturaC());
        boolean aprovada = falhas.isEmpty();
        String motivo = aprovada ? null : String.join("; ", falhas);

        Analise analise = analiseRepository.save(new Analise(req.loteCodigo(), req.ph(), req.acidezTotal(),
                req.teorAlcoolico(), req.temperaturaC(),
                aprovada ? ResultadoAnalise.APROVADA : ResultadoAnalise.REPROVADA, motivo, analista));

        if (aprovada) {
            eventoPublisher.publicar(Eventos.QUALIDADE_APROVADA,
                    new Eventos.QualidadeAprovada(req.loteCodigo(), analise.getId(), Instant.now()));
        } else {
            eventoPublisher.publicar(Eventos.QUALIDADE_REPROVADA,
                    new Eventos.QualidadeReprovada(req.loteCodigo(), analise.getId(), motivo, Instant.now()));
        }
        return analise;
    }

    @Transactional(readOnly = true)
    public List<Analise> listar(String loteCodigo) {
        return loteCodigo == null ? analiseRepository.findAll()
                : analiseRepository.findByLoteCodigoOrderByIdDesc(loteCodigo);
    }

    /** Chamado pelo listener do evento lote.criado. Idempotente. */
    @Transactional
    public void registrarLoteConhecido(String codigo, String uva) {
        if (!loteConhecidoRepository.existsById(codigo)) {
            loteConhecidoRepository.save(new LoteConhecido(codigo, uva));
        }
    }
}
