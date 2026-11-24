package br.com.vinheria.pedidos.messaging;

import java.math.BigDecimal;
import java.time.Instant;


public final class Eventos {

    private Eventos() {}

    public static final String EXCHANGE = "vinheria.eventos";
    public static final String DLX = "vinheria.eventos.dlx";
    public static final String DLQ = "vinheria.eventos.dlq";

    public static final String PEDIDO_CONFIRMADO = "pedido.confirmado";
    public static final String PEDIDO_CANCELADO = "pedido.cancelado";

    public record PedidoConfirmado(Long pedidoId, String clienteUsername, BigDecimal valorTotal, Instant ocorridoEm) {}

    public record PedidoCancelado(Long pedidoId, String clienteUsername, Instant ocorridoEm) {}
}
