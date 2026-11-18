package br.com.vinheria.pedidos.pedido;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    private String sku;
    private int quantidade;
    private BigDecimal precoUnitario; // preenchido quando o Estoque confirma a reserva
}
