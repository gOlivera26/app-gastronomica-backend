package com.example.appgastronomica.controllers;

import com.example.appgastronomica.dtos.common.PedidoResponse;
import com.example.appgastronomica.services.PedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pedidos")
@Slf4j
@RestController
public class PedidoController {
    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/crearPedido")
    public ResponseEntity<PedidoResponse> crearPedido(@RequestBody PedidoResponse pedido) {
        PedidoResponse pedidoCreado = pedidoService.crearPedido(pedido);
        if(pedidoCreado == null){
            log.warn("No se pudo crear el pedido");
            return ResponseEntity.badRequest().build();
        }
        log.info("Pedido creado: {}", pedidoCreado);
        return ResponseEntity.ok(pedidoCreado);
    }
}
