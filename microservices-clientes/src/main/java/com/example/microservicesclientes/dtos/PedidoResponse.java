package com.example.microservicesclientes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {
    private Long idPedido;
    private Long idCliente;
    private String nombre;
    private String apellido;
    private String nroDoc;
    private LocalDateTime fechaPedido;
    private String direccionEntrega;
    private String estado;
    private String observacion;
    private Double total;
    private String nroTurno;
    private List<DetallePedidoResponse> detallePedido;


    @Override
    public String toString() {
        return "Nombre Completo: "+nombre+" "+apellido;
    }
}
