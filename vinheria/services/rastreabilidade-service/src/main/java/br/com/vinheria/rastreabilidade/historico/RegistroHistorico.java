package br.com.vinheria.rastreabilidade.historico;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

/** Linha do histórico de um lote. Append-only: registros nunca são alterados nem removidos. */
@Entity
@Table(name = "historico_lote", indexes = @Index(columnList = "loteCodigo"))
@Getter
@NoArgsConstructor
public class RegistroHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loteCodigo;

    /** Routing key do evento, ex.: lote.criado, qualidade.reprovada. */
    private String tipoEvento;

    /** Conteúdo original do evento, guardado como JSON. */
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> detalhes;

    private Instant registradoEm;

    public RegistroHistorico(String loteCodigo, String tipoEvento, Map<String, Object> detalhes) {
        this.loteCodigo = loteCodigo;
        this.tipoEvento = tipoEvento;
        this.detalhes = detalhes;
        this.registradoEm = Instant.now();
    }
}
