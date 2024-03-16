package com.example.appgastronomica.controllers;

import com.example.appgastronomica.dtos.common.PedidoRequest;
import com.example.appgastronomica.dtos.common.PedidoResponse;
import com.example.appgastronomica.services.PedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<PedidoRequest> crearPedido(@RequestBody PedidoRequest pedido) {
        PedidoRequest pedidoCreado = pedidoService.crearPedido(pedido);
        if(pedidoCreado == null){
            log.warn("No se pudo crear el pedido");
            return ResponseEntity.badRequest().build();
        }
        log.info("Pedido creado: {}", pedidoCreado);
        return ResponseEntity.ok(pedidoCreado);
    }

    @GetMapping("/obtenerPedidos")
    public ResponseEntity<List<PedidoResponse>> obtenerPedidos() {
        List<PedidoResponse> pedidos = pedidoService.obtenerPedidos();
        if(pedidos.isEmpty()){
            log.warn("No se encontraron pedidos");
            return ResponseEntity.noContent().build();
        }
        log.info("Pedidos encontrados: {}", pedidos);
        return ResponseEntity.ok(pedidos);
    }

}
