package com.example.appgastronomica.controllers;

import com.example.appgastronomica.dtos.common.StockDto;
import com.example.appgastronomica.entities.ProductoEntity;
import com.example.appgastronomica.entities.TipoProductoEntity;
import com.example.appgastronomica.models.Producto;
import com.example.appgastronomica.services.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/producto")
public class ProductoController {
    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    @PostMapping("/crearProducto")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto productoCreado = productoService.crearProducto(producto);
        if(productoCreado == null){
            log.warn("No se pudo crear el producto");
            return ResponseEntity.badRequest().build();
        }
        log.info("Producto creado: {}", productoCreado);
        return ResponseEntity.ok(productoCreado);
    }

    @DeleteMapping("/eliminarProducto/{id}")
    public ResponseEntity<Producto> eliminarProducto(@PathVariable Long id) {
        Producto productoEliminado = productoService.eliminarProductoPorId(id);
        if(productoEliminado == null){
            log.warn("No se pudo eliminar el producto");
            return ResponseEntity.badRequest().build();
        }
        log.info("Producto eliminado: {}", productoEliminado);
        return ResponseEntity.ok(productoEliminado);
    }
    @PutMapping("/modificarProducto")
    public ResponseEntity<Producto> modificarProducto(@RequestBody Producto producto) {
        Producto productoModificado = productoService.modificarProducto(producto);
        if(productoModificado == null){
            log.warn("No se pudo modificar el producto");
            return ResponseEntity.badRequest().build();
        }
        log.info("Producto modificado: {}", productoModificado);
        return ResponseEntity.ok(productoModificado);
    }

    @GetMapping("/obtenerProductos")
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productos = productoService.obtenerProductos();
        if(productos.isEmpty()){
            log.warn("No se encontraron productos");
            return ResponseEntity.badRequest().build();
        }
        log.info("Productos encontrados: {}", productos);
        return ResponseEntity.ok(productos);
    }
    @PostMapping("/actualizarStock")
    public ResponseEntity<String> actualizarStock(@RequestBody StockDto producto) {
        if(producto == null){
            log.warn("No se pudo actualizar el stock");
            return ResponseEntity.badRequest().build();
        }
        productoService.actualizarStock(producto);
        log.info("Stock actualizado: {}", producto);
        return ResponseEntity.ok("Stock actualizado");
    }
    @PostMapping("/actualizarPrecioGlobal")
    public ResponseEntity<String> actualizarPrecioGlobal(@RequestParam Double porcentajeAumento) {
        if(porcentajeAumento == null){
            log.warn("No se pudo actualizar el precio global");
            return ResponseEntity.badRequest().build();
        }
        productoService.actualizarPrecioGlobal(porcentajeAumento);
        log.info("Precio global actualizado: {}", porcentajeAumento);
        return ResponseEntity.ok("Precio global actualizado");
    }

    @GetMapping("/obtenerProductoPorTipo/{id}")
    public ResponseEntity<List<Producto>> obtenerProductoPorTipo(@PathVariable Long id) {
        List<Producto> productos = productoService.obtenerProductoPorTipo(id);
        if(productos.isEmpty()){
            log.warn("No se encontraron productos");
            return ResponseEntity.badRequest().build();
        }
        log.info("Productos encontrados: {}", productos);
        return ResponseEntity.ok(productos);
    }

    //Tipo Producto
    @PostMapping("/crearTipoProducto")
    public ResponseEntity<TipoProductoEntity> crearTipoProducto(@RequestBody TipoProductoEntity producto) {
        TipoProductoEntity productoCreado = productoService.crearTipoProducto(producto);
        if(productoCreado == null){
            log.warn("No se pudo crear el Tipo Producto");
            return ResponseEntity.badRequest().build();
        }
        log.info("Tipo Producto creado: {}", productoCreado);
        return ResponseEntity.ok(productoCreado);
    }

    @DeleteMapping("/eliminarTipoProducto/{id}")
    public ResponseEntity<TipoProductoEntity> eliminarTipoProducto(@PathVariable Long id) {
        TipoProductoEntity productoEliminado = productoService.eliminarTipoProductoPorId(id);
        if(productoEliminado == null){
            log.warn("No se pudo eliminar el Tipo Producto");
            return ResponseEntity.badRequest().build();
        }
        log.info("Tipo Producto eliminado: {}", productoEliminado);
        return ResponseEntity.ok(productoEliminado);
    }

    @PutMapping("/modificarTipoProducto")
    public ResponseEntity<TipoProductoEntity> modificarTipoProducto(@RequestBody TipoProductoEntity producto) {
        TipoProductoEntity productoModificado = productoService.modificarTipoProducto(producto);
        if(productoModificado == null){
            log.warn("No se pudo modificar el Tipo Producto");
            return ResponseEntity.badRequest().build();
        }
        log.info("Tipo Producto modificado: {}", productoModificado);
        return ResponseEntity.ok(productoModificado);
    }

    @GetMapping("/obtenerTipoProductos")
    public ResponseEntity<List<TipoProductoEntity>> obtenerTipoProductos() {
        List<TipoProductoEntity> productos = productoService.obtenerTipoProductos();
        if(productos.isEmpty()){
            log.warn("No se encontraron Tipo Producto");
            return ResponseEntity.badRequest().build();
        }
        log.info("Tipo Producto encontrados: {}", productos);
        return ResponseEntity.ok(productos);
    }

}
