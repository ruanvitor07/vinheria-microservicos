package br.com.vinheria.identidade.usuario;

public record UsuarioResponse(Long id, String username, String nome, String perfil) {

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getUsername(), u.getNome(), u.getPerfil().name());
    }
}
