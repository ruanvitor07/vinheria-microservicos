package br.com.vinheria.rastreabilidade.messaging;


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

    // Rastreabilidade ouve TODOS os eventos de lote e de qualidade (só guarda o histórico).
    public static final String LOTE_TODOS = "lote.*";
    public static final String QUALIDADE_TODOS = "qualidade.*";
}
