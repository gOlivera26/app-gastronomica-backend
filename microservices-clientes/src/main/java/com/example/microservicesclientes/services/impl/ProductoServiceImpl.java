package com.example.microservicesclientes.services.impl;

import com.example.microservicesclientes.dtos.StockDto;
import com.example.microservicesclientes.entities.ProductoEntity;
import com.example.microservicesclientes.entities.TipoProductoEntity;
import com.example.microservicesclientes.models.Producto;
import com.example.microservicesclientes.repository.ProductoRepository;
import com.example.microservicesclientes.repository.TipoProductoRepository;
import com.example.microservicesclientes.services.ProductoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final TipoProductoRepository tipoProductoRepository;
    private final ProductoRepository productoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductoServiceImpl(TipoProductoRepository tipoProductoRepository, ModelMapper modelMapper, ProductoRepository productoRepository) {
        this.tipoProductoRepository = tipoProductoRepository;
        this.modelMapper = modelMapper;
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto crearProducto(Producto producto) {
        if(producto.getId() == null){
            throw new RuntimeException("El ID no puede ser nulo");
        }
        Optional<TipoProductoEntity> tipoProducto = tipoProductoRepository.findById(producto.getTipoProducto().getId());
        if(tipoProducto.isEmpty()){
            throw new RuntimeException("El tipo de producto no existe");
        }
        ProductoEntity productoEntity = modelMapper.map(producto, ProductoEntity.class);
        return modelMapper.map(productoRepository.save(productoEntity), Producto.class);
    }

    @Override
    public Producto eliminarProductoPorId(Long id) {
        Optional<ProductoEntity> productoEntity = productoRepository.findById(id);
        if(productoEntity.isEmpty()){
            throw new RuntimeException("El producto no existe");
        }
        productoRepository.delete(productoEntity.get());
        return modelMapper.map(productoEntity.get(), Producto.class);
    }

    @Override
    public Producto modificarProducto(Producto producto) {
        if(producto.getId() == null){
            throw new RuntimeException("El ID no puede ser nulo");
        }
        Optional<ProductoEntity> productoEntity = productoRepository.findById(producto.getId());
        if(productoEntity.isEmpty()){
            throw new RuntimeException("El producto no existe");
        }
        Optional<TipoProductoEntity> tipoProducto = tipoProductoRepository.findById(producto.getTipoProducto().getId());
        if(tipoProducto.isEmpty()){
            throw new RuntimeException("El tipo de producto no existe");
        }
        ProductoEntity productoEntity1 = modelMapper.map(producto, ProductoEntity.class);
        return modelMapper.map(productoRepository.save(productoEntity1), Producto.class);
    }

    @Override
    public List<Producto> obtenerProductos() {
        List<ProductoEntity> productos = productoRepository.findAll();
        if(productos.isEmpty()){
            throw new RuntimeException("No se encontraron productos");
        }
        return productos.stream().map(producto -> modelMapper.map(producto, Producto.class)).toList();
    }

    @Override
    public List<Producto> obtenerProductoPorTipo(Long id) {
        Optional<TipoProductoEntity> tipoProducto = tipoProductoRepository.findById(id);
        if(tipoProducto.isEmpty()){
            throw new RuntimeException("El tipo de producto no existe");
        }
        List<ProductoEntity> productos = productoRepository.findByTipoProducto(tipoProducto.get());
        if(productos.isEmpty()){
            throw new RuntimeException("No se encontraron productos");
        }
        return productos.stream().map(producto -> modelMapper.map(producto, Producto.class)).toList();
    }

    @Override
    public List<Producto> obtenerProductoNovedad() {
        Optional<ProductoEntity> producto = productoRepository.findByNovedadTrue();
        if(producto.isEmpty()){
            throw new RuntimeException("No se encontraron productos");
        }
        return List.of(modelMapper.map(producto.get(), Producto.class));
    }

    @Override
    public void actualizarStock(StockDto stockDto) {
        Optional<ProductoEntity> productoEntity = productoRepository.findById(stockDto.getIdProducto());
        if(productoEntity.isEmpty()){
            throw new RuntimeException("El producto no existe");
        }
        ProductoEntity producto = productoEntity.get();
        producto.setStock(stockDto.getStock());
        productoRepository.save(producto);
    }

    @Override
    public void actualizarPrecioGlobal(Double porcentajeAumento) {
        List<ProductoEntity> productos = productoRepository.findAll();
        BigDecimal porcentajeDecimal = BigDecimal.valueOf(porcentajeAumento / 100.0);

        productos.forEach(producto -> {
            BigDecimal precioActual = BigDecimal.valueOf(producto.getPrecio());
            BigDecimal aumento = precioActual.multiply(porcentajeDecimal);
            BigDecimal nuevoPrecio = precioActual.add(aumento);

            //Redondear precio
            nuevoPrecio = nuevoPrecio.setScale(2, RoundingMode.HALF_UP);

            producto.setPrecio(nuevoPrecio.doubleValue());
            productoRepository.save(producto);
        });
    }


    @Override
    public TipoProductoEntity crearTipoProducto(TipoProductoEntity producto) {
        if(producto.getId() == null) {
            throw new RuntimeException("El ID no puede ser nulo");
        }
        TipoProductoEntity tipoProducto = modelMapper.map(producto, TipoProductoEntity.class);
        return tipoProductoRepository.save(tipoProducto);
    }

    @Override
    public TipoProductoEntity eliminarTipoProductoPorId(Long id) {
        Optional<TipoProductoEntity> tipoProducto = tipoProductoRepository.findById(id);
        if(tipoProducto.isEmpty()){
            throw new RuntimeException("El tipo de producto no existe");
        }
        tipoProductoRepository.delete(tipoProducto.get());
        return modelMapper.map(tipoProducto.get(), TipoProductoEntity.class);
    }

    @Override
    public TipoProductoEntity modificarTipoProducto(TipoProductoEntity producto) {
        if(producto.getId() == null){
            throw new RuntimeException("El ID no puede ser nulo");
        }
        Optional<TipoProductoEntity> tipoProducto = tipoProductoRepository.findById(producto.getId());
        if(tipoProducto.isEmpty()){
            throw new RuntimeException("El tipo de producto no existe");
        }
        TipoProductoEntity tipoProductoEntity = modelMapper.map(producto, TipoProductoEntity.class);
        return tipoProductoRepository.save(tipoProductoEntity);
    }

    @Override
    public List<TipoProductoEntity> obtenerTipoProductos() {
        List<TipoProductoEntity> tipoProductoEntities = tipoProductoRepository.findAll();
        if(tipoProductoEntities.isEmpty()){
            throw new RuntimeException("No se encontraron tipos de productos");
        }
        return tipoProductoEntities;
    }


}
