package br.com.vinheria.pedidos.pedido;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clienteUsername;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private BigDecimal valorTotal;
    private String motivoRecusa;
    private Instant criadoEm;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pedido_itens", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido(String clienteUsername, List<ItemPedido> itens) {
        this.clienteUsername = clienteUsername;
        this.itens = new ArrayList<>(itens);
        this.status = StatusPedido.PENDENTE;
        this.criadoEm = Instant.now();
    }

    public void confirmar(BigDecimal valorTotal, List<ItemPedido> itensComPreco) {
        this.itens.clear();
        this.itens.addAll(itensComPreco);
        this.valorTotal = valorTotal;
        this.status = StatusPedido.CONFIRMADO;
    }

    public void recusar(String motivo) {
        this.status = StatusPedido.RECUSADO;
        this.motivoRecusa = motivo;
    }

    public void cancelar() {
        if (status != StatusPedido.CONFIRMADO) {
            throw new IllegalStateException("Só é possível cancelar pedidos CONFIRMADOS (status atual: " + status + ")");
        }
        this.status = StatusPedido.CANCELADO;
    }
}
