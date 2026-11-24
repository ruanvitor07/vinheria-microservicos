package br.com.vinheria.pedidos.estoque;

public class EstoqueRecusouException extends RuntimeException {
    public EstoqueRecusouException(String mensagem) {
        super(mensagem);
    }
}
