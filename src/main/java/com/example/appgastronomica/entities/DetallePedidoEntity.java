package com.example.appgastronomica.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoEntity producto;

    @Column
    private Integer cantidad;
    @Column
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY) // Un detalle de pedido pertenece a un pedido
    @JoinColumn(name = "pedido_id")
    private PedidoEntity pedido;
}
