package br.com.vinheria.qualidade.analise;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Regras (simplificadas e didáticas) de aceitação de um lote.
 * Classe sem Spring de propósito: fácil de ler e de testar.
 */
public final class RegrasQualidade {

    private RegrasQualidade() {}

    /** Retorna a lista de motivos de reprovação; lista vazia significa lote aprovado. */
    public static List<String> verificar(BigDecimal ph, BigDecimal acidezTotal,
                                         BigDecimal teorAlcoolico, BigDecimal temperaturaC) {
        List<String> falhas = new ArrayList<>();
        if (foraDaFaixa(ph, "2.9", "3.9")) {
            falhas.add("pH " + ph + " fora da faixa 2,9 a 3,9");
        }
        if (foraDaFaixa(acidezTotal, "4.0", "9.0")) {
            falhas.add("acidez total " + acidezTotal + " g/L fora da faixa 4,0 a 9,0");
        }
        if (foraDaFaixa(teorAlcoolico, "9.0", "15.0")) {
            falhas.add("teor alcoólico " + teorAlcoolico + "% fora da faixa 9,0 a 15,0");
        }
        if (foraDaFaixa(temperaturaC, "10", "30")) {
            falhas.add("temperatura " + temperaturaC + "°C fora da faixa 10 a 30");
        }
        return falhas;
    }

    private static boolean foraDaFaixa(BigDecimal valor, String minimo, String maximo) {
        return valor.compareTo(new BigDecimal(minimo)) < 0 || valor.compareTo(new BigDecimal(maximo)) > 0;
    }
}
