package com.example.microservicesclientes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoResponse {
    private Long idProducto;
    private String producto;
    private Double precioProducto;
    private Integer cantidad;
    private Double subtotal;
}
