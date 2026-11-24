package br.com.vinheria.estoque.reserva;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
@Getter
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long pedidoId;

    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reserva_itens", joinColumns = @JoinColumn(name = "reserva_id"))
    private List<ItemReserva> itens = new ArrayList<>();

    private Instant criadaEm;

    public Reserva(Long pedidoId) {
        this.pedidoId = pedidoId;
        this.status = StatusReserva.ATIVA;
        this.criadaEm = Instant.now();
    }

    public void adicionarItem(String sku, int quantidade, BigDecimal precoUnitario) {
        itens.add(new ItemReserva(sku, quantidade, precoUnitario));
    }

    public BigDecimal valorTotal() {
        return itens.stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cancelar() {
        this.status = StatusReserva.CANCELADA;
    }
}
