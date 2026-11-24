package br.com.vinheria.identidade.auth;

import br.com.vinheria.identidade.auth.dto.LoginRequest;
import br.com.vinheria.identidade.auth.dto.RegistroRequest;
import br.com.vinheria.identidade.auth.dto.TokenResponse;
import br.com.vinheria.identidade.usuario.Perfil;
import br.com.vinheria.identidade.usuario.Usuario;
import br.com.vinheria.identidade.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .filter(u -> passwordEncoder.matches(request.senha(), u.getSenhaHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));
        return tokenService.gerar(usuario);
    }

    public Usuario registrar(RegistroRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já utilizado");
        }
        return usuarioRepository.save(new Usuario(request.username(), request.nome(),
                passwordEncoder.encode(request.senha()), Perfil.CLIENTE));
    }
}
