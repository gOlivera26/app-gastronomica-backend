package com.example.microservicesclientes.controllers;

import com.example.microservicesclientes.configs.CheckoutProConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pruebaPago")
public class PagoController {
    private final CheckoutProConfig checkoutProConfig;

    @Autowired
    public PagoController(CheckoutProConfig checkoutProConfig) {
        this.checkoutProConfig = checkoutProConfig;
    }

    @GetMapping("/prueba/{paymentId}")
    public ResponseEntity<Object> getPagos(@PathVariable Long paymentId){
        return ResponseEntity.ok(checkoutProConfig.getPagos(paymentId));
    }
}
