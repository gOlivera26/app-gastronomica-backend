package com.example.appgastronomica.services;

import com.example.appgastronomica.dtos.common.PedidoResponse;
import org.springframework.stereotype.Service;

@Service
public interface PedidoService {

    PedidoResponse crearPedido(PedidoResponse pedido);
}
