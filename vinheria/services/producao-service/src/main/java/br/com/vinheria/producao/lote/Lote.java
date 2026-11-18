package br.com.vinheria.producao.lote;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/** Lote de vinho em produção. As regras de transição de etapa/status ficam aqui dentro. */
@Entity
@Table(name = "lotes")
@Getter
@NoArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    private String uva;
    private int safra;
    private String fornecedorUva;
    private BigDecimal quantidadeKg;

    /** SKU do produto acabado que este lote vai gerar no estoque. */
    private String sku;
    private String nomeProduto;

    @Enumerated(EnumType.STRING)
    private Etapa etapaAtual;

    @Enumerated(EnumType.STRING)
    private StatusLote status;

    private Integer quantidadeGarrafas;
    private Instant criadoEm;

    public Lote(String codigo, String uva, int safra, String fornecedorUva,
                BigDecimal quantidadeKg, String sku, String nomeProduto) {
        this.codigo = codigo;
        this.uva = uva;
        this.safra = safra;
        this.fornecedorUva = fornecedorUva;
        this.quantidadeKg = quantidadeKg;
        this.sku = sku;
        this.nomeProduto = nomeProduto;
        this.etapaAtual = Etapa.COLHEITA;
        this.status = StatusLote.EM_PRODUCAO;
        this.criadoEm = Instant.now();
    }

    /** Conclui a etapa atual e avança para a próxima. No engarrafamento, o lote é finalizado. */
    public void concluirEtapa(Integer garrafasProduzidas) {
        if (status != StatusLote.EM_PRODUCAO) {
            throw new IllegalStateException("Lote " + codigo + " não está em produção (status: " + status + ")");
        }
        if (etapaAtual == Etapa.ENGARRAFAMENTO) {
            if (garrafasProduzidas == null || garrafasProduzidas <= 0) {
                throw new IllegalArgumentException("Informe a quantidade de garrafas ao concluir o engarrafamento");
            }
            this.quantidadeGarrafas = garrafasProduzidas;
            this.status = StatusLote.FINALIZADO;
        } else {
            this.etapaAtual = etapaAtual.proxima();
        }
    }

    public void colocarEmQuarentena() {
        if (status == StatusLote.EM_PRODUCAO) {
            status = StatusLote.EM_QUARENTENA;
        }
    }

    public void liberarDaQuarentena() {
        if (status == StatusLote.EM_QUARENTENA) {
            status = StatusLote.EM_PRODUCAO;
        }
    }
}
