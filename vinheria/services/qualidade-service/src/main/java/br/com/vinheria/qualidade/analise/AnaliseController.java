package br.com.vinheria.qualidade.analise;

import br.com.vinheria.qualidade.analise.dto.AnaliseResponse;
import br.com.vinheria.qualidade.analise.dto.RegistrarAnaliseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analises")
@RequiredArgsConstructor
public class AnaliseController {

    private final AnaliseService analiseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('LABORATORIO','ADMIN')")
    public AnaliseResponse registrar(@Valid @RequestBody RegistrarAnaliseRequest request, Authentication auth) {
        // auth.getName() é o "sub" do JWT: quem fez a análise fica registrado
        return AnaliseResponse.from(analiseService.registrar(request, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('LABORATORIO','OPERADOR','ADMIN')")
    public List<AnaliseResponse> listar(@RequestParam(required = false) String loteCodigo) {
        return analiseService.listar(loteCodigo).stream().map(AnaliseResponse::from).toList();
    }
}
