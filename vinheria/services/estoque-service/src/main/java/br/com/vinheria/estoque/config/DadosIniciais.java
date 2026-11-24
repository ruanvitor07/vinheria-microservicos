package br.com.vinheria.estoque.config;

import br.com.vinheria.estoque.produto.Produto;
import br.com.vinheria.estoque.produto.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DadosIniciais implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) {
        if (produtoRepository.count() == 0) {
            produtoRepository.save(new Produto("CAB-2025-750", "Cabernet Sauvignon 2025 750ml", new BigDecimal("89.90"), 20, 5));
            produtoRepository.save(new Produto("MER-2025-750", "Merlot 2025 750ml", new BigDecimal("79.90"), 10, 5));
        }
    }
}
