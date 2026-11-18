package br.com.vinheria.estoque.produto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Produto acabado (garrafa) controlado em estoque. disponivel = saldo - reservado. */
@Entity
@Table(name = "produtos")
@Getter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    private String nome;
    private BigDecimal preco;
    private int saldo;
    private int reservado;
    private int estoqueMinimo;

    public Produto(String sku, String nome, BigDecimal preco, int saldo, int estoqueMinimo) {
        this.sku = sku;
        this.nome = nome;
        this.preco = preco;
        this.saldo = saldo;
        this.estoqueMinimo = estoqueMinimo;
    }

    public int disponivel() {
        return saldo - reservado;
    }

    public void reservar(int quantidade) {
        if (quantidade > disponivel()) {
            throw new IllegalStateException("Estoque insuficiente para " + sku
                    + " (solicitado: " + quantidade + ", disponível: " + disponivel() + ")");
        }
        reservado += quantidade;
    }

    public void liberar(int quantidade) {
        reservado = Math.max(0, reservado - quantidade);
    }

    public void registrarEntrada(int quantidade) {
        saldo += quantidade;
    }

    public boolean estoqueBaixo() {
        return disponivel() <= estoqueMinimo;
    }
}
