package com.example.microservicesclientes.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private Long id;
    private Cliente cliente;
    private LocalDateTime fechaPedido;
    private String direccionEntrega;
    private Double total;
    private Boolean estado;
    private String observacion;
    private String numeroTurno;
    private List<DetallePedido> detallePedido;
}
