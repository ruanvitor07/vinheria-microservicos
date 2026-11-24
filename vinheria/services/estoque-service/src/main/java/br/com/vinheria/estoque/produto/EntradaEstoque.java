package br.com.vinheria.estoque.produto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Table(name = "entradas_estoque")
@Getter
@NoArgsConstructor
public class EntradaEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loteCodigo;

    private String sku;
    private int quantidade;
    private Instant criadaEm;

    public EntradaEstoque(String loteCodigo, String sku, int quantidade) {
        this.loteCodigo = loteCodigo;
        this.sku = sku;
        this.quantidade = quantidade;
        this.criadaEm = Instant.now();
    }
}
