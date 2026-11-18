package br.com.vinheria.identidade.auth.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
}
