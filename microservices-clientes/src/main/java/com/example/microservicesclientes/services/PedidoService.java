package com.example.microservicesclientes.services;

import com.example.microservicesclientes.dtos.DetallePedidoRequest;
import com.example.microservicesclientes.dtos.PedidoRequest;
import com.example.microservicesclientes.dtos.PedidoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PedidoService {

    PedidoRequest crearPedido(PedidoRequest pedido);
    String actualizarEstadoPedido(Long idPedido, String estado);
    void eliminarPedido(Long idPedido);
    void eliminarPedidosCancelados();
    List<PedidoResponse> obtenerPedidos();
    DetallePedidoRequest crearDetallePedido(DetallePedidoRequest detallePedido, Long idPedido);
    DetallePedidoRequest eliminarDetallePedido(Long idDetallePedido);
    DetallePedidoRequest editarDetallePedido(Long idDetallePedido, DetallePedidoRequest detallePedidoRequest);
}
