package com.example.microservicesclientes.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoRequest {

    private Long idCliente;
    private LocalDateTime fechaPedido;
    private String direccionEntrega;
    private String observacion;
    private List<DetallePedidoRequest> detallePedido;
}
