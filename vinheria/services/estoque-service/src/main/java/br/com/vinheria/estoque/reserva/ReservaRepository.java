package br.com.vinheria.estoque.reserva;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findByPedidoId(Long pedidoId);
}
