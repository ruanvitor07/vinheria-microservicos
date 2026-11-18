package br.com.vinheria.qualidade;

import br.com.vinheria.qualidade.analise.RegrasQualidade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RegrasQualidadeTest {

    private static BigDecimal n(String v) {
        return new BigDecimal(v);
    }

    @Test
    void deveAprovarQuandoTodosOsValoresEstaoNaFaixa() {
        List<String> falhas = RegrasQualidade.verificar(n("3.4"), n("6.0"), n("13.0"), n("22"));
        assertThat(falhas).isEmpty();
    }

    @Test
    void deveReprovarComPhAlto() {
        List<String> falhas = RegrasQualidade.verificar(n("4.2"), n("6.0"), n("13.0"), n("22"));
        assertThat(falhas).hasSize(1);
        assertThat(falhas.get(0)).contains("pH");
    }

    @Test
    void deveListarTodosOsMotivosDeReprovacao() {
        List<String> falhas = RegrasQualidade.verificar(n("4.2"), n("2.0"), n("20"), n("35"));
        assertThat(falhas).hasSize(4);
    }
}
