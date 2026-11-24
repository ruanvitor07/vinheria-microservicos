package br.com.vinheria.producao.messaging;

import br.com.vinheria.producao.config.RabbitConfig;
import br.com.vinheria.producao.lote.LoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class QualidadeListener {

    private final LoteService loteService;

    @RabbitListener(queues = RabbitConfig.FILA_QUALIDADE_REPROVADA)
    public void aoReprovar(Eventos.QualidadeReprovada evento) {
        log.info("Qualidade reprovou o lote {}: {}", evento.loteCodigo(), evento.motivo());
        loteService.colocarEmQuarentena(evento.loteCodigo());
    }

    @RabbitListener(queues = RabbitConfig.FILA_QUALIDADE_APROVADA)
    public void aoAprovar(Eventos.QualidadeAprovada evento) {
        log.info("Qualidade aprovou o lote {}", evento.loteCodigo());
        loteService.liberarDaQuarentena(evento.loteCodigo());
    }
}
