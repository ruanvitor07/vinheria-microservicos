package br.com.vinheria.estoque.produto;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findBySku(String sku);

    boolean existsBySku(String sku);

    /**
     * Busca com lock de escrita: duas reservas simultâneas do mesmo SKU não conseguem
     * "gastar" o mesmo saldo. Ordenado por SKU para evitar deadlock entre transações.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.sku in :skus order by p.sku")
    List<Produto> buscarParaAtualizar(@Param("skus") Collection<String> skus);
}
