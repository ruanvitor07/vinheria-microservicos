package br.com.vinheria.producao.messaging;

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

    // Publicados por Produção
    public static final String LOTE_CRIADO = "lote.criado";
    public static final String LOTE_ETAPA_CONCLUIDA = "lote.etapa-concluida";
    public static final String LOTE_ENGARRAFADO = "lote.engarrafado";

    // Consumidos de Qualidade
    public static final String QUALIDADE_APROVADA = "qualidade.aprovada";
    public static final String QUALIDADE_REPROVADA = "qualidade.reprovada";

    public record LoteCriado(String loteCodigo, String uva, int safra, String fornecedorUva,
                             BigDecimal quantidadeKg, Instant ocorridoEm) {}

    public record EtapaConcluida(String loteCodigo, String etapa, Instant ocorridoEm) {}

    public record LoteEngarrafado(String loteCodigo, String sku, String nomeProduto,
                                  int quantidadeGarrafas, Instant ocorridoEm) {}

    public record QualidadeAprovada(String loteCodigo, Long analiseId) {}

    public record QualidadeReprovada(String loteCodigo, Long analiseId, String motivo) {}
}
