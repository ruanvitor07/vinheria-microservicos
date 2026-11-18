package br.com.vinheria.identidade.auth;

import br.com.vinheria.identidade.auth.dto.TokenResponse;
import br.com.vinheria.identidade.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final long expiracaoEmSegundos;

    public TokenService(JwtEncoder jwtEncoder, @Value("${app.jwt.expiration-seconds:3600}") long expiracaoEmSegundos) {
        this.jwtEncoder = jwtEncoder;
        this.expiracaoEmSegundos = expiracaoEmSegundos;
    }

    public TokenResponse gerar(Usuario usuario) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("vinheria-identidade")
                .subject(usuario.getUsername())
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expiracaoEmSegundos))
                .claim("roles", List.of(usuario.getPerfil().name()))
                .claim("nome", usuario.getNome())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", expiracaoEmSegundos);
    }
}
