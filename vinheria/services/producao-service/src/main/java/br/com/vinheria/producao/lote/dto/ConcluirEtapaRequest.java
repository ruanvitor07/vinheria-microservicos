package br.com.vinheria.producao.lote.dto;

/** Só é obrigatório ao concluir o ENGARRAFAMENTO. */
public record ConcluirEtapaRequest(Integer quantidadeGarrafas) {
}
