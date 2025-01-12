package com.example.microservicesclientes.services;

import com.example.microservicesclientes.dtos.StockDto;
import com.example.microservicesclientes.entities.ProductoEntity;
import com.example.microservicesclientes.entities.TipoProductoEntity;
import com.example.microservicesclientes.models.Producto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductoService {
    Producto crearProducto(Producto producto);
    Producto eliminarProductoPorId(Long id);
    Producto modificarProducto(Producto producto);
    ProductoEntity obtenerProductoPorId(Long id);
    List<Producto> obtenerProductos();
    List<Producto> obtenerProductoPorTipo(Long id);
    List<Producto> obtenerProductoNovedad();
    void actualizarStock(StockDto stockDto);
    void actualizarPrecioGlobal(Double porcentajeAumento);
    TipoProductoEntity crearTipoProducto(TipoProductoEntity producto);
    TipoProductoEntity eliminarTipoProductoPorId(Long id);
    TipoProductoEntity modificarTipoProducto(TipoProductoEntity producto);
    List<TipoProductoEntity> obtenerTipoProductos();
}
