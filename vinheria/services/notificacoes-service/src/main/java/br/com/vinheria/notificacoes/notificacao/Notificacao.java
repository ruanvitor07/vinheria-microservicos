package br.com.vinheria.notificacoes.notificacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notificacoes")
@Getter
@NoArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoEvento;
    private String destinatario;
    private String assunto;

    @Column(length = 1000)
    private String mensagem;

    private Instant criadaEm;

    public Notificacao(String tipoEvento, String destinatario, String assunto, String mensagem) {
        this.tipoEvento = tipoEvento;
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.criadaEm = Instant.now();
    }
}
