package com.example.microservicesclientes.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedido {
    private Long id;
    private List<Producto> productos;
    private Integer cantidad;
    private Double subtotal;
    private Pedido pedido;
}
