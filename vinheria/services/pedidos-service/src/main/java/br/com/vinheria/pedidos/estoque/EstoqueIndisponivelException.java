package br.com.vinheria.pedidos.estoque;

public class EstoqueIndisponivelException extends RuntimeException {
    public EstoqueIndisponivelException(Throwable causa) {
        super("Serviço de estoque indisponível", causa);
    }
}
