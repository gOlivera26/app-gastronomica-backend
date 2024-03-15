package com.example.appgastronomica.services;

import com.example.appgastronomica.dtos.common.StockDto;
import com.example.appgastronomica.entities.ProductoEntity;
import com.example.appgastronomica.entities.TipoProductoEntity;
import com.example.appgastronomica.models.Producto;
import com.example.appgastronomica.models.TipoProducto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductoService {
    Producto crearProducto(Producto producto);
    Producto eliminarProductoPorId(Long id);
    Producto modificarProducto(Producto producto);
    List<Producto> obtenerProductos();
    List<Producto> obtenerProductoPorTipo(Long id);
    void actualizarStock(StockDto stockDto);
    void actualizarPrecioGlobal(Double porcentajeAumento);
    TipoProductoEntity crearTipoProducto(TipoProductoEntity producto);
    TipoProductoEntity eliminarTipoProductoPorId(Long id);
    TipoProductoEntity modificarTipoProducto(TipoProductoEntity producto);
    List<TipoProductoEntity> obtenerTipoProductos();
}
