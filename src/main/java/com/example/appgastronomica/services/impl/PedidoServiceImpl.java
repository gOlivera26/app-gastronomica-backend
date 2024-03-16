package com.example.appgastronomica.services.impl;


import com.example.appgastronomica.dtos.common.DetallePedidoResponse;
import com.example.appgastronomica.dtos.common.PedidoRequest;
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

import java.util.ArrayList;
import java.util.List;
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
    public PedidoRequest crearPedido(PedidoRequest pedidoRequest) {
        try {
            Optional<ClienteEntity> existeCliente = clienteRepository.findById(pedidoRequest.getIdCliente());
            if (!existeCliente.isPresent()) {
                throw new RuntimeException("Cliente no encontrado");
            }

            PedidoEntity pedido = modelMapper.map(pedidoRequest, PedidoEntity.class);

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
            return modelMapper.map(pedidoGuardado, PedidoRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el pedido", e);
        }
    }

    @Override
    public PedidoRequest actualizarPedido(PedidoRequest pedido) {

    }

    @Override
    public List<PedidoResponse> obtenerPedidos() {
        List<PedidoEntity> pedidos = pedidoRepository.findAll();
        List<PedidoResponse> pedidoResponses = new ArrayList<>();

        for (PedidoEntity pedido : pedidos) {
            PedidoResponse pedidoResponse = modelMapper.map(pedido, PedidoResponse.class);
            pedidoResponse.setNombre(pedido.getCliente().getNombre());
            pedidoResponse.setApellido(pedido.getCliente().getApellido());
            pedidoResponse.setNroDoc(pedido.getCliente().getNroDoc());
            pedidoResponse.setDetallePedido(new ArrayList<>());
            for (DetallePedidoEntity detalle : pedido.getDetallePedido()) {
                DetallePedidoResponse detalleResponse = modelMapper.map(detalle, DetallePedidoResponse.class);
                detalleResponse.setProducto(detalle.getProducto().getNombre());
                detalleResponse.setPrecioProducto(this.obtenerPrecioProducto(detalle.getProducto().getId()));
                pedidoResponse.getDetallePedido().add(detalleResponse);
            }
            pedidoResponses.add(pedidoResponse);
        }
        return pedidoResponses;
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
