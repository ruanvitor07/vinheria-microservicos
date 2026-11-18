package br.com.vinheria.qualidade.config;

import br.com.vinheria.qualidade.messaging.Eventos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topologia do RabbitMQ usada por este serviço.
 * Todos usam o mesmo exchange (topic) "vinheria.eventos". Cada consumidor cria a SUA fila
 * e a liga ao exchange pelas routing keys de interesse. Mensagens que falham após as
 * tentativas de retry vão para a Dead Letter Queue (vinheria.eventos.dlq).
 */
@Configuration
public class RabbitConfig {

    public static final String FILA_LOTE_CRIADO = "qualidade.lote-criado";

    @Bean
    Queue filaLoteCriado() {
        return fila(FILA_LOTE_CRIADO);
    }

    @Bean
    Binding bindingLoteCriado() {
        return BindingBuilder.bind(filaLoteCriado()).to(eventosExchange()).with(Eventos.LOTE_CRIADO);
    }

    // ---- Infra comum (declarada de forma idempotente por todos os serviços) ----

    @Bean
    TopicExchange eventosExchange() {
        return new TopicExchange(Eventos.EXCHANGE);
    }

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(Eventos.DLX);
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(Eventos.DLQ).build();
    }

    @Bean
    Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("#");
    }

    /** Eventos trafegam como JSON. O tipo é inferido pelo parâmetro do @RabbitListener. */
    @Bean
    MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        var conversor = new Jackson2JsonMessageConverter(objectMapper);
        conversor.setAlwaysConvertToInferredType(true);
        return conversor;
    }

    private Queue fila(String nome) {
        return QueueBuilder.durable(nome).deadLetterExchange(Eventos.DLX).build();
    }
}
