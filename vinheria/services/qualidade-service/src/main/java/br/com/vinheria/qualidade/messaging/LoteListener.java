package br.com.vinheria.qualidade.messaging;

import br.com.vinheria.qualidade.analise.AnaliseService;
import br.com.vinheria.qualidade.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoteListener {

    private final AnaliseService analiseService;

    @RabbitListener(queues = RabbitConfig.FILA_LOTE_CRIADO)
    public void aoCriarLote(Eventos.LoteCriado evento) {
        log.info("Novo lote conhecido pela Qualidade: {}", evento.loteCodigo());
        analiseService.registrarLoteConhecido(evento.loteCodigo(), evento.uva());
    }
}
