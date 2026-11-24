package br.com.vinheria.pedidos.estoque;

import java.math.BigDecimal;
import java.util.List;

public record ReservaResponse(Long pedidoId, String status, BigDecimal valorTotal, List<Item> itens) {

    public record Item(String sku, int quantidade, BigDecimal precoUnitario) {
    }
}
