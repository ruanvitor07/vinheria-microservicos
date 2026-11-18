package br.com.vinheria.identidade.auth;

import br.com.vinheria.identidade.auth.dto.LoginRequest;
import br.com.vinheria.identidade.auth.dto.RegistroRequest;
import br.com.vinheria.identidade.auth.dto.TokenResponse;
import br.com.vinheria.identidade.usuario.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse registrar(@Valid @RequestBody RegistroRequest request) {
        return UsuarioResponse.from(authService.registrar(request));
    }
}
