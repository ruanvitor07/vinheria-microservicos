package br.com.vinheria.producao.lote.dto;

import br.com.vinheria.producao.lote.Lote;

import java.math.BigDecimal;
import java.time.Instant;

public record LoteResponse(String codigo, String uva, int safra, String fornecedorUva, BigDecimal quantidadeKg,
                           String sku, String nomeProduto, String etapaAtual, String status,
                           Integer quantidadeGarrafas, Instant criadoEm) {

    public static LoteResponse from(Lote l) {
        return new LoteResponse(l.getCodigo(), l.getUva(), l.getSafra(), l.getFornecedorUva(), l.getQuantidadeKg(),
                l.getSku(), l.getNomeProduto(), l.getEtapaAtual().name(), l.getStatus().name(),
                l.getQuantidadeGarrafas(), l.getCriadoEm());
    }
}
