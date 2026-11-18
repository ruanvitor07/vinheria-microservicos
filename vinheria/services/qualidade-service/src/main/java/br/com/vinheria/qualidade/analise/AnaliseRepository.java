package br.com.vinheria.qualidade.analise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnaliseRepository extends JpaRepository<Analise, Long> {
    List<Analise> findByLoteCodigoOrderByIdDesc(String loteCodigo);
}
