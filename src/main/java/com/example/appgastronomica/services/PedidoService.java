package com.example.appgastronomica.services;

import com.example.appgastronomica.dtos.common.DetallePedidoRequest;
import com.example.appgastronomica.dtos.common.PedidoRequest;
import com.example.appgastronomica.dtos.common.PedidoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

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
