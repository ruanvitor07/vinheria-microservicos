package br.com.vinheria.estoque.reserva.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservaRequest(@NotNull Long pedidoId, @NotEmpty List<@Valid Item> itens) {

    public record Item(@NotBlank String sku, @Min(1) int quantidade) {
    }
}
