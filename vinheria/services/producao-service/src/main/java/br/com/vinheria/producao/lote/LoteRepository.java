package br.com.vinheria.producao.lote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    Optional<Lote> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
