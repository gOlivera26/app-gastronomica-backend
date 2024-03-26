package com.example.microserviceusuarios.services.Impl;

import com.example.microserviceusuarios.entities.RolEntity;
import com.example.microserviceusuarios.entities.UsuarioEntity;
import com.example.microserviceusuarios.models.Rol;
import com.example.microserviceusuarios.models.Usuario;
import com.example.microserviceusuarios.repository.RolRepository;
import com.example.microserviceusuarios.repository.UsuarioRepository;
import com.example.microserviceusuarios.services.UsuarioService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Autowired
    public UsuarioServiceImpl(ModelMapper modelMapper, UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.modelMapper = modelMapper;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }


    @Override
    public List<Usuario> obtenerUsuarios() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        if(usuarios.isEmpty()){
            throw new RuntimeException("No se encontraron usuarios");
        }
        return usuarios.stream().map(usuario -> modelMapper.map(usuario, Usuario.class)).toList();
    }

    @Transactional
    @Override
    public List<Usuario> obtenerUsuariosPorRol(Long idRol) {
        Optional<RolEntity> rol = rolRepository.findById(idRol);
        if(rol.isEmpty()){
            throw new RuntimeException("El rol no existe");
        }
        List<UsuarioEntity> usuarios = usuarioRepository.findByRol(rol.get());
        if(usuarios.isEmpty()){
            throw new RuntimeException("No se encontraron usuarios con el rol especificado");
        }
        return usuarios.stream().map(usuario -> modelMapper.map(usuario, Usuario.class)).toList();
    }



    @Override
    public Rol crearRol(Rol rol) {
        if(rol == null){
            throw new RuntimeException("El rol no puede ser nulo");
        }
        List<RolEntity> listRoles = rolRepository.findAll();
        if(listRoles.stream().anyMatch(r -> r.getDescripcion().equals(rol.getDescripcion()))){
            throw new RuntimeException("El rol ya existe");
        }
        RolEntity rolEntity = modelMapper.map(rol, RolEntity.class);
        return modelMapper.map(rolRepository.save(rolEntity), Rol.class);
    }

    @Override
    public Rol eliminarRol(Long id) {
        Optional<RolEntity> roles = rolRepository.findById(id);
        if(roles.isEmpty()){
            throw new RuntimeException("El rol no existe");
        }
        rolRepository.deleteById(id);
        return modelMapper.map(roles.get(), Rol.class);
    }

    @Override
    public Rol editarRol(Rol rol) {
        if(rol == null){
            throw new RuntimeException("El rol no puede ser nulo");
        }
        Optional<RolEntity> roles = rolRepository.findById(rol.getId());
        if(roles.isEmpty()){
            throw new RuntimeException("El rol no existe");
        }
        RolEntity rolEntity = modelMapper.map(rol, RolEntity.class);
        return modelMapper.map(rolRepository.save(rolEntity), Rol.class);
    }

    @Transactional
    @Override
    public List<Rol> obtenerRoles() {
        List<RolEntity> roles = rolRepository.findAll();
        return roles.stream().map(r -> modelMapper.map(r, Rol.class)).toList();
    }
    @Transactional
    @Override
    public Boolean emailExiste(String email) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(email);
        return usuario.isPresent();
    }

    @Transactional
    @Override
    public Boolean usernameExiste(String username) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByUsername(username);
        return usuario.isPresent();
    }

    @Transactional
    @Override
    public Boolean nroDocExiste(String nroDoc) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByNroDoc(nroDoc);
        return usuario.isPresent();
    }

    @Transactional
    @Override
    public Boolean bajaUsuario(String nroDoc) {
        Optional<UsuarioEntity> usuarioOptional = usuarioRepository.findByNroDoc(nroDoc);
        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("El usuario no existe");
        }
        UsuarioEntity usuarioEntity = usuarioOptional.get();
        usuarioEntity.setActivo(!usuarioEntity.getActivo()); //invertir el estado
        usuarioRepository.save(usuarioEntity);
        return usuarioEntity.getActivo();
    }

    @Transactional
    @Override
    public void agregarImagenUsuario(String username, MultipartFile imagen) {
        UsuarioEntity usuario = obtenerUsuarioPorUsername(username);
        if (usuario == null) {
            throw new RuntimeException("El usuario no existe");
        }
        if (imagen.isEmpty()) {
            throw new RuntimeException("La imagen no puede estar vacía");
        }
        try {
            usuario.setImagenProfile(imagen.getBytes());
            usuarioRepository.save(usuario);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen del usuario", e);
        }
    }


    @Transactional
    @Override
    public String obtenerImagenUsuario(String username) {
        UsuarioEntity usuario = obtenerUsuarioPorUsername(username);
        if (usuario.getImagenProfile() == null) {
            throw new RuntimeException("El usuario no tiene imagen");
        }
        return Base64.getEncoder().encodeToString(usuario.getImagenProfile());
    }

    private UsuarioEntity obtenerUsuarioPorNroDoc(String nroDoc) {
        Optional<UsuarioEntity> usuarios = usuarioRepository.findByNroDoc(nroDoc);
        if(usuarios.isEmpty()){
            throw new RuntimeException("No se encontraron usuarios con el nro de documento especificado");
        }
        return usuarios.get();
    }
    @Transactional
    @Override
    public UsuarioEntity obtenerUsuarioPorUsername(String username){
        Optional<UsuarioEntity> usuario = usuarioRepository.findByUsername(username);
        if(usuario.isEmpty()){
            throw new RuntimeException("No se encontraron usuarios con el username especificado");
        }
        return usuario.get();
    }
}
