package br.com.vinheria.identidade.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank String nome,
        @NotBlank @Size(min = 6, max = 100) String senha) {
}
