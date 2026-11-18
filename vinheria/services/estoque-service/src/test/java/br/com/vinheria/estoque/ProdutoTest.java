package br.com.vinheria.estoque;

import br.com.vinheria.estoque.produto.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProdutoTest {

    private Produto produto() {
        return new Produto("CAB-2025-750", "Cabernet", new BigDecimal("89.90"), 20, 5);
    }

    @Test
    void reservaReduzOEstoqueDisponivel() {
        Produto p = produto();
        p.reservar(8);
        assertThat(p.disponivel()).isEqualTo(12);
        assertThat(p.getSaldo()).isEqualTo(20);
    }

    @Test
    void naoPermiteReservarMaisQueODisponivel() {
        Produto p = produto();
        assertThatThrownBy(() -> p.reservar(21)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void liberarDevolveOEstoqueReservado() {
        Produto p = produto();
        p.reservar(10);
        p.liberar(10);
        assertThat(p.disponivel()).isEqualTo(20);
    }

    @Test
    void indicaEstoqueBaixoQuandoDisponivelChegaNoMinimo() {
        Produto p = produto();
        p.reservar(15);
        assertThat(p.estoqueBaixo()).isTrue();
    }
}
