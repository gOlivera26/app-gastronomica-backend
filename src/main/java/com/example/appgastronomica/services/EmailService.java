package com.example.appgastronomica.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void enviarCatalogoProductos(String email, String nroDoc);
}
