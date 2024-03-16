package com.example.appgastronomica.services;

import com.example.appgastronomica.dtos.common.PedidoRequest;
import com.example.appgastronomica.dtos.common.PedidoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PedidoService {

    PedidoRequest crearPedido(PedidoRequest pedido);
    List<PedidoResponse> obtenerPedidos();
}
