package br.com.vinheria.qualidade.analise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "analises")
@Getter
@NoArgsConstructor
public class Analise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loteCodigo;

    private BigDecimal ph;
    private BigDecimal acidezTotal;      // g/L
    private BigDecimal teorAlcoolico;    // % vol
    private BigDecimal temperaturaC;

    @Enumerated(EnumType.STRING)
    private ResultadoAnalise resultado;

    @Column(length = 500)
    private String motivo;

    private String analista;
    private Instant realizadaEm;

    public Analise(String loteCodigo, BigDecimal ph, BigDecimal acidezTotal, BigDecimal teorAlcoolico,
                   BigDecimal temperaturaC, ResultadoAnalise resultado, String motivo, String analista) {
        this.loteCodigo = loteCodigo;
        this.ph = ph;
        this.acidezTotal = acidezTotal;
        this.teorAlcoolico = teorAlcoolico;
        this.temperaturaC = temperaturaC;
        this.resultado = resultado;
        this.motivo = motivo;
        this.analista = analista;
        this.realizadaEm = Instant.now();
    }
}
