package com.example.microservicesclientes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "factura")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura")
    private String numeroFactura;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column
    private Double total;

    @OneToOne(mappedBy = "factura", cascade = CascadeType.ALL)
    private PedidoEntity pedido; // Relación uno a uno con el pedido
}
