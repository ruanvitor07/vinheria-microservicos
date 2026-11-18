package br.com.vinheria.notificacoes.messaging;


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

    public static final String PEDIDO_CONFIRMADO = "pedido.confirmado";
    public static final String PEDIDO_CANCELADO = "pedido.cancelado";
    public static final String ESTOQUE_BAIXO = "estoque.baixo";
    public static final String QUALIDADE_REPROVADA = "qualidade.reprovada";
}
