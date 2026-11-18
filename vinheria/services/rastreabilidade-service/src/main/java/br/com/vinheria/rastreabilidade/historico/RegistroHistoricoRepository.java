package br.com.vinheria.rastreabilidade.historico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroHistoricoRepository extends JpaRepository<RegistroHistorico, Long> {
    List<RegistroHistorico> findByLoteCodigoOrderByIdAsc(String loteCodigo);
}
