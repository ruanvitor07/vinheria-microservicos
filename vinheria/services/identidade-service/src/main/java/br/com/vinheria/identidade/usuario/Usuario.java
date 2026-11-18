package br.com.vinheria.identidade.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Getter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String nome;

    @Column(nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    public Usuario(String username, String nome, String senhaHash, Perfil perfil) {
        this.username = username;
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }
}
