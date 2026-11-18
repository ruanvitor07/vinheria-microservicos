package br.com.vinheria.producao.lote;

/** Etapas da produção, na ordem em que acontecem. */
public enum Etapa {
    COLHEITA, FERMENTACAO, MATURACAO, ENGARRAFAMENTO;

    public Etapa proxima() {
        int indice = ordinal() + 1;
        return indice < values().length ? values()[indice] : null;
    }
}
