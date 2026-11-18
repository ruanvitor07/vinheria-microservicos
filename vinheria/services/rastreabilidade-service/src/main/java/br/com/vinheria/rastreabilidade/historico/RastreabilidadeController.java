package br.com.vinheria.rastreabilidade.historico;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/rastreabilidade")
@RequiredArgsConstructor
public class RastreabilidadeController {

    private final RegistroHistoricoRepository repository;

    /** Reconstrói a linha do tempo de um lote: criação, etapas, análises e engarrafamento. */
    @GetMapping("/lotes/{codigo}")
    @PreAuthorize("hasAnyRole('OPERADOR','LABORATORIO','ADMIN')")
    public List<RegistroHistoricoResponse> historicoDoLote(@PathVariable String codigo) {
        List<RegistroHistorico> historico = repository.findByLoteCodigoOrderByIdAsc(codigo);
        if (historico.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sem histórico para o lote " + codigo);
        }
        return historico.stream().map(RegistroHistoricoResponse::from).toList();
    }
}
