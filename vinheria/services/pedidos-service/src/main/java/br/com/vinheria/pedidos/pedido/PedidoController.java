package br.com.vinheria.pedidos.pedido;

import br.com.vinheria.pedidos.pedido.dto.CriarPedidoRequest;
import br.com.vinheria.pedidos.pedido.dto.PedidoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /** Sempre devolve 201 com o pedido; o campo "status" informa se foi CONFIRMADO ou RECUSADO. */
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody CriarPedidoRequest request,
                                                JwtAuthenticationToken auth) {
        Pedido pedido = pedidoService.criar(request, auth.getName(), auth.getToken().getTokenValue());
        return ResponseEntity.created(URI.create("/api/pedidos/" + pedido.getId())).body(PedidoResponse.from(pedido));
    }

    /** Cliente vê só os seus pedidos; ADMIN vê todos. */
    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public List<PedidoResponse> listar(JwtAuthenticationToken auth) {
        List<Pedido> pedidos = ehAdmin(auth) ? pedidoService.listarTodos() : pedidoService.listarDoCliente(auth.getName());
        return pedidos.stream().map(PedidoResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public PedidoResponse buscar(@PathVariable Long id, JwtAuthenticationToken auth) {
        Pedido pedido = pedidoService.buscar(id);
        garantirAcesso(pedido, auth);
        return PedidoResponse.from(pedido);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public PedidoResponse cancelar(@PathVariable Long id, JwtAuthenticationToken auth) {
        garantirAcesso(pedidoService.buscar(id), auth);
        return PedidoResponse.from(pedidoService.cancelar(id, auth.getToken().getTokenValue()));
    }

    private boolean ehAdmin(JwtAuthenticationToken auth) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void garantirAcesso(Pedido pedido, JwtAuthenticationToken auth) {
        if (!ehAdmin(auth) && !pedido.getClienteUsername().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este pedido pertence a outro cliente");
        }
    }
}
