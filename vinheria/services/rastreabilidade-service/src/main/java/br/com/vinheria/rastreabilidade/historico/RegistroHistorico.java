package br.com.vinheria.rastreabilidade.historico;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

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

    private String tipoEvento;

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
