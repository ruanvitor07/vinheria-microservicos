package br.com.vinheria.notificacoes.messaging;

import br.com.vinheria.notificacoes.config.RabbitConfig;
import br.com.vinheria.notificacoes.notificacao.Notificacao;
import br.com.vinheria.notificacoes.notificacao.NotificacaoRepository;
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

    private static final String EMAIL_COMPRAS = "compras@vinheria.local";
    private static final String EMAIL_PRODUCAO = "producao@vinheria.local";

    private final NotificacaoRepository repository;

    @RabbitListener(queues = RabbitConfig.FILA_EVENTOS)
    public void tratar(Map<String, Object> evento, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        Notificacao notificacao = switch (routingKey) {
            case Eventos.PEDIDO_CONFIRMADO -> new Notificacao(routingKey, texto(evento, "clienteUsername"),
                    "Pedido confirmado",
                    "Seu pedido #" + evento.get("pedidoId") + " foi confirmado. Total: R$ " + evento.get("valorTotal"));
            case Eventos.PEDIDO_CANCELADO -> new Notificacao(routingKey, texto(evento, "clienteUsername"),
                    "Pedido cancelado",
                    "Seu pedido #" + evento.get("pedidoId") + " foi cancelado.");
            case Eventos.ESTOQUE_BAIXO -> new Notificacao(routingKey, EMAIL_COMPRAS,
                    "Alerta de estoque baixo",
                    evento.get("nomeProduto") + " (" + evento.get("sku") + ") tem apenas "
                            + evento.get("disponivel") + " unidades disponíveis.");
            case Eventos.QUALIDADE_REPROVADA -> new Notificacao(routingKey, EMAIL_PRODUCAO,
                    "Lote em quarentena",
                    "O lote " + evento.get("loteCodigo") + " foi reprovado: " + evento.get("motivo"));
            default -> null;
        };

        if (notificacao == null) {
            log.warn("Evento sem notificação associada: {}", routingKey);
            return;
        }
        repository.save(notificacao);
        log.info("[E-MAIL SIMULADO] para={} | assunto={} | {}",
                notificacao.getDestinatario(), notificacao.getAssunto(), notificacao.getMensagem());
    }

    private String texto(Map<String, Object> evento, String campo) {
        Object valor = evento.get(campo);
        return valor == null ? "desconhecido" : valor.toString();
    }
}
