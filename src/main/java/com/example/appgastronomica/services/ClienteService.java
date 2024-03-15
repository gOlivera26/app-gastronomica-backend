package com.example.appgastronomica.services;

import com.example.appgastronomica.models.Cliente;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    Cliente eliminarClientePorNroDoc(String nroDoc);
    Cliente modificarCliente(Cliente cliente);
    Cliente obtenerClientePorNroDoc(String nroDoc);
    List<Cliente> obtenerClientes();

}
