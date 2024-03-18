package com.example.microservicesclientes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private Double precio;
    @Column
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "tipo_producto_id")
    private TipoProductoEntity tipoProducto;
    @Column
    private String imagen;
}
