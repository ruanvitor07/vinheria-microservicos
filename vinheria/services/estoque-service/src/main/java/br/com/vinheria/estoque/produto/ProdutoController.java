package br.com.vinheria.estoque.produto;

import br.com.vinheria.estoque.produto.dto.CriarProdutoRequest;
import br.com.vinheria.estoque.produto.dto.ProdutoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    @GetMapping
    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll().stream().map(ProdutoResponse::from).toList();
    }

    @GetMapping("/{sku}")
    public ProdutoResponse buscar(@PathVariable String sku) {
        return produtoRepository.findBySku(sku).map(ProdutoResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + sku));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody CriarProdutoRequest req) {
        if (produtoRepository.existsBySku(req.sku())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe produto com o SKU " + req.sku());
        }
        Produto produto = produtoRepository.save(new Produto(req.sku(), req.nome(), req.preco(), 0, req.estoqueMinimo()));
        return ResponseEntity.created(URI.create("/api/produtos/" + produto.getSku())).body(ProdutoResponse.from(produto));
    }
}
