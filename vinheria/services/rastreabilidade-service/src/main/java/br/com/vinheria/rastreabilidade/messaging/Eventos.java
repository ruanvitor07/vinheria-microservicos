package br.com.vinheria.rastreabilidade.messaging;

public final class Eventos {

    private Eventos() {}

    public static final String EXCHANGE = "vinheria.eventos";
    public static final String DLX = "vinheria.eventos.dlx";
    public static final String DLQ = "vinheria.eventos.dlq";

    public static final String LOTE_TODOS = "lote.*";
    public static final String QUALIDADE_TODOS = "qualidade.*";
}
