package br.com.vinheria.estoque.reserva;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemReserva {
    private String sku;
    private int quantidade;
    private BigDecimal precoUnitario;
}
