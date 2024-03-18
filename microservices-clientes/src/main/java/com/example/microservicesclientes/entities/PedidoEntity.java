package com.example.microservicesclientes.entities;

import com.example.microservicesclientes.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    @Column
    private Double total;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Column
    private String observacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedidoEntity> detallePedido;
}
