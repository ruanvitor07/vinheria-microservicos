package br.com.vinheria.pedidos;

import br.com.vinheria.pedidos.estoque.EstoqueClient;
import br.com.vinheria.pedidos.estoque.EstoqueRecusouException;
import br.com.vinheria.pedidos.estoque.ReservaResponse;
import br.com.vinheria.pedidos.messaging.EventoPublisher;
import br.com.vinheria.pedidos.messaging.Eventos;
import br.com.vinheria.pedidos.pedido.Pedido;
import br.com.vinheria.pedidos.pedido.PedidoRepository;
import br.com.vinheria.pedidos.pedido.PedidoService;
import br.com.vinheria.pedidos.pedido.StatusPedido;
import br.com.vinheria.pedidos.pedido.dto.CriarPedidoRequest;
import br.com.vinheria.pedidos.pedido.dto.ItemPedidoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock PedidoRepository pedidoRepository;
    @Mock EstoqueClient estoqueClient;
    @Mock EventoPublisher eventoPublisher;
    @InjectMocks PedidoService pedidoService;

    private final CriarPedidoRequest request =
            new CriarPedidoRequest(List.of(new ItemPedidoRequest("CAB-2025-750", 2)));

    @Test
    void deveConfirmarPedidoEPublicarEventoQuandoEstoqueReserva() {
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(estoqueClient.reservar(any(), any(), eq("token"))).thenReturn(new ReservaResponse(
                1L, "ATIVA", new BigDecimal("179.80"),
                List.of(new ReservaResponse.Item("CAB-2025-750", 2, new BigDecimal("89.90")))));

        Pedido pedido = pedidoService.criar(request, "cliente", "token");

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CONFIRMADO);
        assertThat(pedido.getValorTotal()).isEqualByComparingTo("179.80");
        verify(eventoPublisher).publicar(eq(Eventos.PEDIDO_CONFIRMADO), any());
    }

    @Test
    void deveRecusarPedidoSemPublicarEventoQuandoEstoqueRecusa() {
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(estoqueClient.reservar(any(), any(), any()))
                .thenThrow(new EstoqueRecusouException("Estoque insuficiente"));

        Pedido pedido = pedidoService.criar(request, "cliente", "token");

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.RECUSADO);
        assertThat(pedido.getMotivoRecusa()).isEqualTo("Estoque insuficiente");
        verify(eventoPublisher, never()).publicar(any(), any());
    }
}
