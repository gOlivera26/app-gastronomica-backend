package com.example.microservicesclientes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoRequest {
    private Long idProducto;
    private Integer cantidad;
}
