package br.com.vinheria.qualidade.analise;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Cópia local e mínima dos lotes que a Produção criou (alimentada pelo evento lote.criado).
 * Assim a Qualidade valida o código do lote sem fazer chamada REST à Produção.
 */
@Entity
@Table(name = "lotes_conhecidos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoteConhecido {
    @Id
    private String codigo;
    private String uva;
}
