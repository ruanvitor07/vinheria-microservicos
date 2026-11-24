package br.com.vinheria.estoque.reserva;

import br.com.vinheria.estoque.reserva.dto.ReservaRequest;
import br.com.vinheria.estoque.reserva.dto.ReservaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final EstoqueService estoqueService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ReservaResponse reservar(@Valid @RequestBody ReservaRequest request) {
        return ReservaResponse.from(estoqueService.reservar(request));
    }

    @DeleteMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public void liberar(@PathVariable Long pedidoId) {
        estoqueService.liberarReserva(pedidoId);
    }
}
