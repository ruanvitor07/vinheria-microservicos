package br.com.vinheria.pedidos.estoque;

import br.com.vinheria.pedidos.pedido.dto.ItemPedidoRequest;

import java.util.List;

public record ReservaRequest(Long pedidoId, List<ItemPedidoRequest> itens) {
}
