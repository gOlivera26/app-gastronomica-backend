package com.example.microservicesclientes.services.impl;

import com.example.microservicesclientes.entities.ClienteEntity;
import com.example.microservicesclientes.models.Cliente;
import com.example.microservicesclientes.repository.ClienteRepository;
import com.example.microservicesclientes.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ModelMapper modelMapper;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ModelMapper modelMapper, ClienteRepository clienteRepository){
        this.modelMapper = modelMapper;
        this.clienteRepository = clienteRepository;
    }
    @Override
    public Cliente crearCliente(Cliente cliente) {
        if (!cliente.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$") || !cliente.getApellido().matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            throw new RuntimeException("El nombre y el apellido solo pueden contener letras y espacios");
        }
        Optional<ClienteEntity> clienteEntities = clienteRepository.findByNroDoc(cliente.getNroDoc());
        if(clienteEntities.isPresent()){
            throw new RuntimeException("El numero de documento ya pertenece a un cliente");
        }
        clienteEntities = clienteRepository.findByTelefono(cliente.getTelefono());
        if(clienteEntities.isPresent()){
            throw new RuntimeException("El telefono ya pertenece a un cliente");
        }
        clienteEntities = clienteRepository.findByEmail(cliente.getEmail());
        if(clienteEntities.isPresent()){
            throw new RuntimeException("El email ya esta en uso");
        }

        ClienteEntity clienteEntity = modelMapper.map(cliente, ClienteEntity.class);
        return modelMapper.map(clienteRepository.save(clienteEntity), Cliente.class);
    }

    @Override
    public Cliente eliminarClientePorNroDoc(String nroDoc) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findByNroDoc(nroDoc);
        if(clienteEntity.isEmpty()){
            throw new RuntimeException("El cliente no existe");
        }
        clienteRepository.delete(clienteEntity.get());
        return modelMapper.map(clienteEntity.get(), Cliente.class);
    }

    @Override
    public Cliente modificarCliente(Cliente cliente) {
        if (!cliente.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$") || !cliente.getApellido().matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$")) {
            throw new RuntimeException("El nombre y el apellido solo pueden contener letras y espacios");
        }
        if(cliente.getId() == null){
            throw new RuntimeException("El ID no puede ser nulo");
        }
        Optional<ClienteEntity> clienteEntityOptional = clienteRepository.findById(cliente.getId());
        if(clienteEntityOptional.isPresent()){
            ClienteEntity clienteEntity = clienteEntityOptional.get();
            if(!clienteEntity.getNroDoc().equals(cliente.getNroDoc()) && clienteRepository.findByNroDoc(cliente.getNroDoc()).isPresent()){
                throw new RuntimeException("El numero de documento ya pertenece a un cliente");
            }
            if(!clienteEntity.getTelefono().equals(cliente.getTelefono()) && clienteRepository.findByTelefono(cliente.getTelefono()).isPresent()){
                throw new RuntimeException("El telefono ya pertenece a un cliente");
            }
            if(!clienteEntity.getEmail().equals(cliente.getEmail()) && clienteRepository.findByEmail(cliente.getEmail()).isPresent()){
                throw new RuntimeException("El email ya esta en uso");
            }
            clienteEntity.setNombre(cliente.getNombre());
            clienteEntity.setApellido(cliente.getApellido());
            clienteEntity.setNroDoc(cliente.getNroDoc());
            clienteEntity.setTelefono(cliente.getTelefono());
            clienteEntity.setEmail(cliente.getEmail());

            ClienteEntity clienteGuardado = clienteRepository.save(clienteEntity);
            return modelMapper.map(clienteGuardado, Cliente.class);
        }
        else{
            throw new RuntimeException("El cliente no existe");
        }
    }

    @Override
    public Cliente obtenerClientePorNroDoc(String nroDoc) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findByNroDoc(nroDoc);
        if(clienteEntity.isEmpty()){
            throw new RuntimeException("El cliente no existe");
        }
        return modelMapper.map(clienteEntity.get(), Cliente.class);
    }


    @Override
    public List<Cliente> obtenerClientes() {
        List<ClienteEntity> clienteEntities = clienteRepository.findAll();
        return clienteEntities.stream().map(clienteEntity -> modelMapper.map(clienteEntity, Cliente.class)).toList();
    }

}
