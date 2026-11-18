package br.com.vinheria.producao.lote.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CriarLoteRequest(
        @NotBlank String codigo,
        @NotBlank String uva,
        @NotNull @Min(2000) Integer safra,
        @NotBlank String fornecedorUva,
        @NotNull @Positive BigDecimal quantidadeKg,
        @NotBlank String sku,
        @NotBlank String nomeProduto) {
}
