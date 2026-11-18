package br.com.vinheria.estoque.reserva.dto;

import br.com.vinheria.estoque.reserva.Reserva;

import java.math.BigDecimal;
import java.util.List;

public record ReservaResponse(Long pedidoId, String status, BigDecimal valorTotal, List<Item> itens) {

    public record Item(String sku, int quantidade, BigDecimal precoUnitario) {
    }

    public static ReservaResponse from(Reserva r) {
        List<Item> itens = r.getItens().stream()
                .map(i -> new Item(i.getSku(), i.getQuantidade(), i.getPrecoUnitario())).toList();
        return new ReservaResponse(r.getPedidoId(), r.getStatus().name(), r.valorTotal(), itens);
    }
}
