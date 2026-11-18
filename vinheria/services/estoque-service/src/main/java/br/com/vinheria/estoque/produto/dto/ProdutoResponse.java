package br.com.vinheria.estoque.produto.dto;

import br.com.vinheria.estoque.produto.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(String sku, String nome, BigDecimal preco, int saldo, int reservado,
                              int disponivel, int estoqueMinimo) {

    public static ProdutoResponse from(Produto p) {
        return new ProdutoResponse(p.getSku(), p.getNome(), p.getPreco(), p.getSaldo(), p.getReservado(),
                p.disponivel(), p.getEstoqueMinimo());
    }
}
