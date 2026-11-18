package br.com.vinheria.pedidos.estoque;

import br.com.vinheria.pedidos.pedido.dto.ItemPedidoRequest;

import java.util.List;

/** Corpo enviado ao Estoque (contrato do POST /api/reservas). */
public record ReservaRequest(Long pedidoId, List<ItemPedidoRequest> itens) {
}
