package br.com.vinheria.notificacoes.messaging;



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
