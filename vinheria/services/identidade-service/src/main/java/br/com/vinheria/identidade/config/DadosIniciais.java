package br.com.vinheria.identidade.config;

import br.com.vinheria.identidade.usuario.Perfil;
import br.com.vinheria.identidade.usuario.Usuario;
import br.com.vinheria.identidade.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Usuários de demonstração (somente para desenvolvimento!). */
@Component
@RequiredArgsConstructor
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        criarSeNaoExistir("admin", "Administrador", "admin123", Perfil.ADMIN);
        criarSeNaoExistir("operador", "Operador de Adega", "operador123", Perfil.OPERADOR);
        criarSeNaoExistir("laboratorio", "Analista de Laboratório", "lab123", Perfil.LABORATORIO);
        criarSeNaoExistir("cliente", "Cliente Demo", "cliente123", Perfil.CLIENTE);
    }

    private void criarSeNaoExistir(String username, String nome, String senha, Perfil perfil) {
        if (!usuarioRepository.existsByUsername(username)) {
            usuarioRepository.save(new Usuario(username, nome, passwordEncoder.encode(senha), perfil));
        }
    }
}
