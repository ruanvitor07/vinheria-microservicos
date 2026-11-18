package br.com.vinheria.estoque.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Publica eventos no RabbitMQ. Se houver transação aberta, o evento só é enviado
 * DEPOIS do commit — assim não anunciamos algo que foi desfeito (rollback).
 * (Versão simplificada do Outbox Pattern: veja o README.)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(String routingKey, Object evento) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    enviar(routingKey, evento);
                }
            });
        } else {
            enviar(routingKey, evento);
        }
    }

    private void enviar(String routingKey, Object evento) {
        rabbitTemplate.convertAndSend(Eventos.EXCHANGE, routingKey, evento);
        log.info("Evento publicado [{}]: {}", routingKey, evento);
    }
}
