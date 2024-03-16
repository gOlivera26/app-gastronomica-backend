package com.example.appgastronomica.services.impl;

import com.example.appgastronomica.dtos.common.DetallePedidoResponse;
import com.example.appgastronomica.dtos.common.PedidoResponse;
import com.example.appgastronomica.entities.ClienteEntity;
import com.example.appgastronomica.entities.DetallePedidoEntity;
import com.example.appgastronomica.entities.PedidoEntity;
import com.example.appgastronomica.entities.ProductoEntity;
import com.example.appgastronomica.repository.ClienteRepository;
import com.example.appgastronomica.repository.DetallePedidoRepository;
import com.example.appgastronomica.repository.PedidoRepository;
import com.example.appgastronomica.repository.ProductoRepository;
import com.example.appgastronomica.services.PedidoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoRepository productoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ModelMapper modelMapper, ClienteRepository clienteRepository, DetallePedidoRepository detallePedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.modelMapper = modelMapper;
        this.clienteRepository = clienteRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public PedidoResponse crearPedido(PedidoResponse pedidoResponse) {
        try {
            Optional<ClienteEntity> existeCliente = clienteRepository.findById(pedidoResponse.getIdCliente());
            if (!existeCliente.isPresent()) {
                throw new RuntimeException("Cliente no encontrado");
            }

            PedidoEntity pedido = modelMapper.map(pedidoResponse, PedidoEntity.class);

            double totalPedido = 0.0;
            for (DetallePedidoEntity detalle : pedido.getDetallePedido()) {
                if (detalle.getCantidad() <= 0) {
                    throw new RuntimeException("La cantidad no puede ser menor o igual a 0");
                }
                if (detalle.getProducto().getId() == null) {
                    throw new RuntimeException("El producto no puede ser nulo");
                }
                double precioProducto = obtenerPrecioProducto(detalle.getProducto().getId());
                double subtotalDetalle = precioProducto * detalle.getCantidad();
                totalPedido += subtotalDetalle;
                detalle.setSubtotal(subtotalDetalle);
                detalle.setPedido(pedido);
            }
            pedido.setTotal(totalPedido);

            PedidoEntity pedidoGuardado = pedidoRepository.save(pedido);
            return modelMapper.map(pedidoGuardado, PedidoResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el pedido", e);
        }
    }


    private double obtenerPrecioProducto(Long idProducto) {
        Optional<ProductoEntity> productos = productoRepository.findById(idProducto);
        if(productos.isPresent()){
            return productos.get().getPrecio();
        }
        else{
            throw new RuntimeException("Producto no encontrado");
        }
    }
}
