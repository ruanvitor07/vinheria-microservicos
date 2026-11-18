package br.com.vinheria.pedidos.estoque;

/** O Estoque respondeu 4xx: regra de negócio (sem saldo, produto inexistente...). */
public class EstoqueRecusouException extends RuntimeException {
    public EstoqueRecusouException(String mensagem) {
        super(mensagem);
    }
}
