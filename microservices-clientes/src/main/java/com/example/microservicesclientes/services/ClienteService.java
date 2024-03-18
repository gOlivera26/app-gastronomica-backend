package com.example.microservicesclientes.services;

import com.example.microservicesclientes.models.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    Cliente eliminarClientePorNroDoc(String nroDoc);
    Cliente modificarCliente(Cliente cliente);
    Cliente obtenerClientePorNroDoc(String nroDoc);
    List<Cliente> obtenerClientes();

}
