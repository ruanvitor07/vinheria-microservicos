package br.com.vinheria.rastreabilidade.config;

import br.com.vinheria.rastreabilidade.messaging.Eventos;
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


@Configuration
public class RabbitConfig {

    public static final String FILA_EVENTOS = "rastreabilidade.eventos";

    @Bean
    Queue filaEventos() {
        return fila(FILA_EVENTOS);
    }

    @Bean
    Binding bindingLote() {
        return BindingBuilder.bind(filaEventos()).to(eventosExchange()).with(Eventos.LOTE_TODOS);
    }

    @Bean
    Binding bindingQualidade() {
        return BindingBuilder.bind(filaEventos()).to(eventosExchange()).with(Eventos.QUALIDADE_TODOS);
    }


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
