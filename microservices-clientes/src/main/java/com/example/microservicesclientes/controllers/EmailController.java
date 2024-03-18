package com.example.microservicesclientes.controllers;

import com.example.microservicesclientes.models.Cliente;
import com.example.microservicesclientes.services.ClienteService;
import com.example.microservicesclientes.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;
    private final ClienteService clienteService;

    @Autowired
    public EmailController(EmailService emailService, ClienteService clienteService) {
        this.emailService = emailService;
        this.clienteService = clienteService;
    }

    @PostMapping("/enviarCatalogoProductos")
    public void enviarCatalogoProductos(String email, String nroDoc) {
        try {
            emailService.enviarCatalogoProductos(email, nroDoc);
            log.info("Catálogo de productos enviado a cliente con correo {} y número de documento {}", email, nroDoc);
        } catch (Exception e) {
            log.error("Error al enviar el catálogo al cliente con correo {} y número de documento {}: {}", email, nroDoc, e.getMessage(), e);
        }
    }

    @PostMapping("/enviarCatalogoProductosATodosClientes")
    public ResponseEntity<String> enviarCatalogoProductosATodosClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerClientes();
            for (Cliente cliente : clientes) {
                emailService.enviarCatalogoProductos(cliente.getEmail(), cliente.getNroDoc());
                log.info("Catálogo de productos enviado a cliente con correo {} y número de documento {}", cliente.getEmail(), cliente.getNroDoc());
            }

            return ResponseEntity.ok("Catálogo de productos enviado exitosamente a todos los clientes.");
        } catch (Exception e) {
            log.error("Error al enviar el catálogo a todos los clientes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el catálogo a todos los clientes: " + e.getMessage());
        }
    }
}
