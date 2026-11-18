package br.com.vinheria.estoque.messaging;

import br.com.vinheria.estoque.config.RabbitConfig;
import br.com.vinheria.estoque.reserva.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoteListener {

    private final EstoqueService estoqueService;

    @RabbitListener(queues = RabbitConfig.FILA_LOTE_ENGARRAFADO)
    public void aoEngarrafarLote(Eventos.LoteEngarrafado evento) {
        estoqueService.registrarEntradaDeProducao(evento);
    }
}
