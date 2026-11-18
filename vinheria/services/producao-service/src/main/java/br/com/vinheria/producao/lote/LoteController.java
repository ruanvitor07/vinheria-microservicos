package br.com.vinheria.producao.lote;

import br.com.vinheria.producao.lote.dto.ConcluirEtapaRequest;
import br.com.vinheria.producao.lote.dto.CriarLoteRequest;
import br.com.vinheria.producao.lote.dto.LoteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
public class LoteController {

    private final LoteService loteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR','ADMIN')")
    public ResponseEntity<LoteResponse> criar(@Valid @RequestBody CriarLoteRequest request) {
        Lote lote = loteService.criar(request);
        return ResponseEntity.created(URI.create("/api/lotes/" + lote.getCodigo())).body(LoteResponse.from(lote));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR','LABORATORIO','ADMIN')")
    public List<LoteResponse> listar() {
        return loteService.listar().stream().map(LoteResponse::from).toList();
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAnyRole('OPERADOR','LABORATORIO','ADMIN')")
    public LoteResponse buscar(@PathVariable String codigo) {
        return LoteResponse.from(loteService.buscarPorCodigo(codigo));
    }

    /** Ação de negócio modelada como sub-recurso: conclui a etapa atual do lote. */
    @PostMapping("/{codigo}/etapas/concluir")
    @PreAuthorize("hasAnyRole('OPERADOR','ADMIN')")
    public LoteResponse concluirEtapa(@PathVariable String codigo,
                                      @RequestBody(required = false) ConcluirEtapaRequest request) {
        Integer garrafas = request == null ? null : request.quantidadeGarrafas();
        return LoteResponse.from(loteService.concluirEtapaAtual(codigo, garrafas));
    }
}
