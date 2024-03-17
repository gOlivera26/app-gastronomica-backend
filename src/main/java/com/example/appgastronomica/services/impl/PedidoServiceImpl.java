package com.example.appgastronomica.services.impl;


import com.example.appgastronomica.dtos.common.DetallePedidoRequest;
import com.example.appgastronomica.dtos.common.DetallePedidoResponse;
import com.example.appgastronomica.dtos.common.PedidoRequest;
import com.example.appgastronomica.dtos.common.PedidoResponse;
import com.example.appgastronomica.entities.ClienteEntity;
import com.example.appgastronomica.entities.DetallePedidoEntity;
import com.example.appgastronomica.entities.PedidoEntity;
import com.example.appgastronomica.entities.ProductoEntity;
import com.example.appgastronomica.enums.EstadoPedido;
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
            pedido.setEstado(EstadoPedido.REGISTRADO);

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
    public String actualizarEstadoPedido(Long idPedido, String estado) {
        Optional<PedidoEntity> pedido = pedidoRepository.findById(idPedido);
        if(pedido.isPresent()){
            pedido.get().setEstado(EstadoPedido.valueOf(estado));
            pedidoRepository.save(pedido.get());
            return "Estado del pedido actualizado";
        }
        else{
            throw new RuntimeException("Pedido no encontrado");
        }
    }

    @Override
    public void eliminarPedido(Long idPedido) {
        Optional<PedidoEntity> pedido = pedidoRepository.findById(idPedido);
        if(pedido.isPresent()){
            pedidoRepository.delete(pedido.get());
        }
        else{
            throw new RuntimeException("Pedido no encontrado");
        }
    }

    @Override
    public void eliminarPedidosCancelados() {
        List<PedidoEntity> pedidos = pedidoRepository.findByEstado(EstadoPedido.CANCELADO);
        if(!pedidos.isEmpty()){
            pedidoRepository.deleteAll(pedidos);
        }
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
            //obtener el nombre del producto y el precio
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

    @Override
    public DetallePedidoRequest crearDetallePedido(DetallePedidoRequest detallePedido, Long idPedido) {
        try {
            Optional<PedidoEntity> optionalPedido = pedidoRepository.findById(idPedido);
            if (optionalPedido.isPresent()) {
                PedidoEntity pedido = optionalPedido.get();

                DetallePedidoEntity detalle = modelMapper.map(detallePedido, DetallePedidoEntity.class);
                detalle.setPedido(pedido);
                double precioProducto = obtenerPrecioProducto(detalle.getProducto().getId());
                double subtotalDetalle = precioProducto * detalle.getCantidad();
                detalle.setSubtotal(subtotalDetalle);
                detallePedidoRepository.save(detalle);

                //actualizar el monto total del pedido
                double totalPedido = calcularTotalPedido(pedido.getDetallePedido());
                pedido.setTotal(totalPedido);
                pedidoRepository.save(pedido);

                return modelMapper.map(detalle, DetallePedidoRequest.class);
            } else {
                throw new RuntimeException("Pedido no encontrado");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el detalle del pedido", e);
        }
    }

    @Override
    public DetallePedidoRequest eliminarDetallePedido(Long idDetallePedido) {
        try {
            Optional<DetallePedidoEntity> detalleOptional = detallePedidoRepository.findById(idDetallePedido);
            if(detalleOptional.isPresent()){
                DetallePedidoEntity detalle = detalleOptional.get();
                detallePedidoRepository.delete(detalle);

                Optional<PedidoEntity> pedidoOptional = pedidoRepository.findById(detalle.getPedido().getId());
                if (pedidoOptional.isPresent()) {
                    PedidoEntity pedido = pedidoOptional.get();
                    List<DetallePedidoEntity> detallesPedido = pedido.getDetallePedido();
                    detallesPedido.remove(detalle);

                    //actualizar el monto total del pedido restando el subtotal eliminado
                    double totalPedido = calcularTotalPedido(detallesPedido);
                    pedido.setTotal(totalPedido);
                    pedidoRepository.save(pedido);
                } else {
                    throw new RuntimeException("Pedido asociado al detalle no encontrado");
                }

                return modelMapper.map(detalle, DetallePedidoRequest.class);
            } else {
                throw new RuntimeException("Detalle no encontrado");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el detalle del pedido", e);
        }
    }

    private double calcularTotalPedido(List<DetallePedidoEntity> detallesPedido) {
        double total = 0.0;
        for (DetallePedidoEntity detalle : detallesPedido) {
            total += detalle.getSubtotal();
        }
        return total;
    }

    @Override
    public DetallePedidoRequest editarDetallePedido(Long idDetallePedido, DetallePedidoRequest detallePedidoRequest) {
        try {
            Optional<DetallePedidoEntity> detalleOptional = detallePedidoRepository.findById(idDetallePedido);
            if(detalleOptional.isPresent()){
                DetallePedidoEntity detalle = detalleOptional.get();

                // Actualizar los campos del detalle con los nuevos valores
                detalle.setCantidad(detallePedidoRequest.getCantidad());
                detalle.setProducto(productoRepository.findById(detallePedidoRequest.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado")));

                // Calcular el nuevo subtotal
                double precioProducto = obtenerPrecioProducto(detalle.getProducto().getId());
                double subtotalDetalle = precioProducto * detalle.getCantidad();
                detalle.setSubtotal(subtotalDetalle);

                // Guardar el detalle actualizado
                DetallePedidoEntity detalleActualizado = detallePedidoRepository.save(detalle);

                // Buscar el pedido asociado al detalle
                Optional<PedidoEntity> pedidoOptional = pedidoRepository.findById(detalle.getPedido().getId());
                if (pedidoOptional.isPresent()) {
                    PedidoEntity pedido = pedidoOptional.get();
                    List<DetallePedidoEntity> detallesPedido = pedido.getDetallePedido();

                    // Recalcular el monto total del pedido
                    double totalPedido = calcularTotalPedido(detallesPedido);
                    pedido.setTotal(totalPedido);
                    pedidoRepository.save(pedido);
                } else {
                    throw new RuntimeException("Pedido asociado al detalle no encontrado");
                }

                return modelMapper.map(detalleActualizado, DetallePedidoRequest.class);
            } else {
                throw new RuntimeException("Detalle no encontrado");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al editar el detalle del pedido", e);
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
