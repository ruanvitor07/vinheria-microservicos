package br.com.vinheria.producao;

import br.com.vinheria.producao.lote.Etapa;
import br.com.vinheria.producao.lote.Lote;
import br.com.vinheria.producao.lote.StatusLote;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoteTest {

    private Lote novoLote() {
        return new Lote("L-001", "Cabernet", 2025, "Fazenda Serra", new BigDecimal("1000"),
                "CAB-2025-750", "Cabernet Sauvignon 2025 750ml");
    }

    @Test
    void deveAvancarAsEtapasNaOrdem() {
        Lote lote = novoLote();
        lote.concluirEtapa(null);
        assertThat(lote.getEtapaAtual()).isEqualTo(Etapa.FERMENTACAO);
        lote.concluirEtapa(null);
        assertThat(lote.getEtapaAtual()).isEqualTo(Etapa.MATURACAO);
    }

    @Test
    void naoDeveAvancarLoteEmQuarentena() {
        Lote lote = novoLote();
        lote.colocarEmQuarentena();
        assertThatThrownBy(() -> lote.concluirEtapa(null)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deveFinalizarAoConcluirEngarrafamento() {
        Lote lote = novoLote();
        lote.concluirEtapa(null);
        lote.concluirEtapa(null);
        lote.concluirEtapa(null);
        lote.concluirEtapa(120);
        assertThat(lote.getStatus()).isEqualTo(StatusLote.FINALIZADO);
        assertThat(lote.getQuantidadeGarrafas()).isEqualTo(120);
    }

    @Test
    void engarrafamentoExigeQuantidadeDeGarrafas() {
        Lote lote = novoLote();
        lote.concluirEtapa(null);
        lote.concluirEtapa(null);
        lote.concluirEtapa(null);
        assertThatThrownBy(() -> lote.concluirEtapa(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void liberarDaQuarentenaVoltaParaProducao() {
        Lote lote = novoLote();
        lote.colocarEmQuarentena();
        lote.liberarDaQuarentena();
        assertThat(lote.getStatus()).isEqualTo(StatusLote.EM_PRODUCAO);
    }
}
