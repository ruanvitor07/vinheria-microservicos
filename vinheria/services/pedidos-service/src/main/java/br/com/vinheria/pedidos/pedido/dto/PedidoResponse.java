package br.com.vinheria.pedidos.pedido.dto;

import br.com.vinheria.pedidos.pedido.Pedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PedidoResponse(Long id, String clienteUsername, String status, BigDecimal valorTotal,
                             String motivoRecusa, Instant criadoEm, List<Item> itens) {

    public record Item(String sku, int quantidade, BigDecimal precoUnitario) {
    }

    public static PedidoResponse from(Pedido p) {
        List<Item> itens = p.getItens().stream()
                .map(i -> new Item(i.getSku(), i.getQuantidade(), i.getPrecoUnitario())).toList();
        return new PedidoResponse(p.getId(), p.getClienteUsername(), p.getStatus().name(), p.getValorTotal(),
                p.getMotivoRecusa(), p.getCriadoEm(), itens);
    }
}
