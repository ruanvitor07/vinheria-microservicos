package br.com.vinheria.pedidos.estoque;

import java.math.BigDecimal;
import java.util.List;

/** Resposta do Estoque. Contém os preços praticados no momento da reserva. */
public record ReservaResponse(Long pedidoId, String status, BigDecimal valorTotal, List<Item> itens) {

    public record Item(String sku, int quantidade, BigDecimal precoUnitario) {
    }
}
