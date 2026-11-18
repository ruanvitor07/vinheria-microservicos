package br.com.vinheria.estoque.produto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaEstoqueRepository extends JpaRepository<EntradaEstoque, Long> {
    boolean existsByLoteCodigo(String loteCodigo);
}
