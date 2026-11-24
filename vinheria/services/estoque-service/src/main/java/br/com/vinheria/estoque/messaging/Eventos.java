package br.com.vinheria.estoque.messaging;

import java.math.BigDecimal;
import java.time.Instant;


public final class Eventos {

    private Eventos() {}

    public static final String EXCHANGE = "vinheria.eventos";
    public static final String DLX = "vinheria.eventos.dlx";
    public static final String DLQ = "vinheria.eventos.dlq";

    // Consumido de Produção
    public static final String LOTE_ENGARRAFADO = "lote.engarrafado";

    // Publicado por Estoque
    public static final String ESTOQUE_BAIXO = "estoque.baixo";

    public record LoteEngarrafado(String loteCodigo, String sku, String nomeProduto, int quantidadeGarrafas) {}

    public record EstoqueBaixo(String sku, String nomeProduto, int disponivel, int estoqueMinimo, Instant ocorridoEm) {}
}
