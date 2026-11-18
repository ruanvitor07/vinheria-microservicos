package br.com.vinheria.qualidade.analise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RegistrarAnaliseRequest(
        @NotBlank String loteCodigo,
        @NotNull @Positive BigDecimal ph,
        @NotNull @Positive BigDecimal acidezTotal,
        @NotNull @Positive BigDecimal teorAlcoolico,
        @NotNull @Positive BigDecimal temperaturaC) {
}
