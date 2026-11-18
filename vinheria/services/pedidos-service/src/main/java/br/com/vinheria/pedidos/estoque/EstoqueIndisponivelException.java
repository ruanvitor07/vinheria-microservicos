package br.com.vinheria.pedidos.estoque;

/** O Estoque não respondeu (timeout, conexão recusada ou 5xx). */
public class EstoqueIndisponivelException extends RuntimeException {
    public EstoqueIndisponivelException(Throwable causa) {
        super("Serviço de estoque indisponível", causa);
    }
}
