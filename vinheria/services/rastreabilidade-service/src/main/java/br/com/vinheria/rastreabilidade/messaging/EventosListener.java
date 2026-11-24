package br.com.vinheria.rastreabilidade.messaging;

import br.com.vinheria.rastreabilidade.config.RabbitConfig;
import br.com.vinheria.rastreabilidade.historico.RegistroHistorico;
import br.com.vinheria.rastreabilidade.historico.RegistroHistoricoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventosListener {

    private final RegistroHistoricoRepository repository;

    @RabbitListener(queues = RabbitConfig.FILA_EVENTOS)
    public void registrar(Map<String, Object> evento,
                          @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        Object loteCodigo = evento.get("loteCodigo");
        if (loteCodigo == null) {
            log.warn("Evento {} ignorado: sem loteCodigo", routingKey);
            return;
        }
        repository.save(new RegistroHistorico(loteCodigo.toString(), routingKey, evento));
        log.info("Histórico do lote {} atualizado: {}", loteCodigo, routingKey);
    }
}
