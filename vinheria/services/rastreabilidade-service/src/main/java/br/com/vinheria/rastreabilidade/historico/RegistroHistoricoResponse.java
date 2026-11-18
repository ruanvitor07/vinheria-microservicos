package br.com.vinheria.rastreabilidade.historico;

import java.time.Instant;
import java.util.Map;

public record RegistroHistoricoResponse(String tipoEvento, Instant registradoEm, Map<String, Object> detalhes) {

    public static RegistroHistoricoResponse from(RegistroHistorico r) {
        return new RegistroHistoricoResponse(r.getTipoEvento(), r.getRegistradoEm(), r.getDetalhes());
    }
}
