package com.example.appgastronomica.controllers;

import com.example.appgastronomica.dtos.common.DetallePedidoRequest;
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

    @PutMapping("/actualizarEstadoPedido/{idPedido}/{estado}")
    public ResponseEntity<String> actualizarEstadoPedido(@PathVariable Long idPedido, @PathVariable String estado) {
        String estadoActualizado = pedidoService.actualizarEstadoPedido(idPedido, estado);
        if(estadoActualizado == null){
            log.warn("No se pudo actualizar el estado del pedido");
            return ResponseEntity.badRequest().build();
        }
        log.info("Estado actualizado: {}", estadoActualizado);
        return ResponseEntity.ok(estadoActualizado);
    }
    @DeleteMapping("/eliminarPedido/{idPedido}")
    public ResponseEntity<String> eliminarPedido(@PathVariable Long idPedido) {
        pedidoService.eliminarPedido(idPedido);
        log.info("Pedido eliminado: {}", idPedido);
        return ResponseEntity.ok("Pedido eliminado");
    }
    @DeleteMapping("/eliminarPedidosCancelados")
    public ResponseEntity<String> eliminarPedidosCancelados() {
        pedidoService.eliminarPedidosCancelados();
        log.info("Pedidos eliminados");
        return ResponseEntity.ok("Pedidos eliminados");
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

    @PostMapping("/crearDetallePedido/{idPedido}")
    public ResponseEntity<DetallePedidoRequest> crearDetallePedido(@RequestBody DetallePedidoRequest pedido, @PathVariable Long idPedido) {
        DetallePedidoRequest pedidoCreado = pedidoService.crearDetallePedido(pedido, idPedido);
        if(pedidoCreado == null){
            log.warn("No se pudo crear el pedido");
            return ResponseEntity.badRequest().build();
        }
        log.info("Pedido creado: {}", pedidoCreado);
        return ResponseEntity.ok(pedidoCreado);
    }
    @DeleteMapping("/eliminarDetallePedido/{idDetallePedido}")
    public ResponseEntity<DetallePedidoRequest> eliminarDetallePedido(@PathVariable Long idDetallePedido) {
        DetallePedidoRequest pedidoEliminado = pedidoService.eliminarDetallePedido(idDetallePedido);
        if(pedidoEliminado == null){
            log.warn("No se pudo eliminar el pedido");
            return ResponseEntity.badRequest().build();
        }
        log.info("Pedido eliminado: {}", pedidoEliminado);
        return ResponseEntity.ok(pedidoEliminado);
    }
    @PutMapping("/editarDetallePedido/{idDetallePedido}")
    public ResponseEntity<DetallePedidoRequest> editarDetallePedido(@RequestBody DetallePedidoRequest pedido, @PathVariable Long idDetallePedido) {
        DetallePedidoRequest pedidoEditado = pedidoService.editarDetallePedido(idDetallePedido, pedido);
        if(pedidoEditado == null){
            log.warn("No se pudo editar el pedido");
            return ResponseEntity.badRequest().build();
        }
        log.info("Pedido editado: {}", pedidoEditado);
        return ResponseEntity.ok(pedidoEditado);
    }

}
