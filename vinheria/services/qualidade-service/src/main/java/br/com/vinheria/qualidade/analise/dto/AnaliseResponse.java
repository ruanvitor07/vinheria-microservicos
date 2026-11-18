package br.com.vinheria.qualidade.analise.dto;

import br.com.vinheria.qualidade.analise.Analise;

import java.math.BigDecimal;
import java.time.Instant;

public record AnaliseResponse(Long id, String loteCodigo, BigDecimal ph, BigDecimal acidezTotal,
                              BigDecimal teorAlcoolico, BigDecimal temperaturaC, String resultado,
                              String motivo, String analista, Instant realizadaEm) {

    public static AnaliseResponse from(Analise a) {
        return new AnaliseResponse(a.getId(), a.getLoteCodigo(), a.getPh(), a.getAcidezTotal(),
                a.getTeorAlcoolico(), a.getTemperaturaC(), a.getResultado().name(),
                a.getMotivo(), a.getAnalista(), a.getRealizadaEm());
    }
}
