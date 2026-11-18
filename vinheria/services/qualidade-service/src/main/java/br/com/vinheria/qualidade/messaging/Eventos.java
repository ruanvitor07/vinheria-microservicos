package br.com.vinheria.qualidade.messaging;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Contrato dos eventos usados por este serviço: nomes de exchange/routing keys e os payloads.
 * Cada serviço mantém a SUA cópia dos eventos que publica/consome (sem biblioteca compartilhada),
 * o que preserva o baixo acoplamento. Consumidores ignoram campos que não conhecem.
 */
public final class Eventos {

    private Eventos() {}

    public static final String EXCHANGE = "vinheria.eventos";
    public static final String DLX = "vinheria.eventos.dlx";
    public static final String DLQ = "vinheria.eventos.dlq";

    // Consumido de Produção
    public static final String LOTE_CRIADO = "lote.criado";

    // Publicados por Qualidade
    public static final String QUALIDADE_APROVADA = "qualidade.aprovada";
    public static final String QUALIDADE_REPROVADA = "qualidade.reprovada";

    public record LoteCriado(String loteCodigo, String uva) {}

    public record QualidadeAprovada(String loteCodigo, Long analiseId, Instant ocorridoEm) {}

    public record QualidadeReprovada(String loteCodigo, Long analiseId, String motivo, Instant ocorridoEm) {}
}
