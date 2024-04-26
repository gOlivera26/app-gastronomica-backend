package com.example.microservicesclientes.services.impl;

import com.example.microservicesclientes.configs.CheckoutProConfig;
import com.example.microservicesclientes.entities.DetallePedidoEntity;
import com.example.microservicesclientes.entities.PedidoEntity;
import com.example.microservicesclientes.entities.ProductoEntity;
import com.example.microservicesclientes.services.PedidoService;
import com.example.microservicesclientes.services.ProductoService;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutProService {
    private final ProductoService productoService;
    private final CheckoutProConfig checkoutProConfig;

    @Autowired
    public CheckoutProService(ProductoService productoService, CheckoutProConfig checkoutProConfig) {
        this.productoService = productoService;
        this.checkoutProConfig = checkoutProConfig;
    }
    public String createPreference(PedidoEntity pedido) {
        // Use Mercado Pago SDK to create PreferenceClient
        PreferenceClient client = new PreferenceClient();


        List<PreferenceItemRequest> items = new ArrayList<>();
        for (DetallePedidoEntity detalle : pedido.getDetallePedido()) {
            ProductoEntity producto = productoService.obtenerProductoPorId(detalle.getProducto().getId());
            items.add(PreferenceItemRequest.builder()
                    .id(detalle.getProducto().getId().toString())
                    .title(producto.getNombre())
                    .description(producto.getDescripcion())
                    .categoryId(producto.getTipoProducto().getId().toString())
                    .quantity(detalle.getCantidad())
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(producto.getPrecio()))
                    .build());
        }

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        try {
            // Send preference request using Mercado Pago SDK
            System.out.println("Access token: " + checkoutProConfig.getAccessToken());
            Preference preference = client.create(preferenceRequest);
            System.out.println("Preference response: " + preference.toString());
            // Return checkout URL
            return preference.getInitPoint();
        } catch (MPException | MPApiException e) {
            e.printStackTrace();
            System.out.println("MercadoPago API error: " + e.getMessage());
            return null;
        }
    }


}
