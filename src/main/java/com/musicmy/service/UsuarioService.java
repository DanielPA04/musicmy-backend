package com.musicmy.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musicmy.entity.UsuarioEntity;
import com.musicmy.exception.ResourceNotFoundException;
import com.musicmy.exception.UnauthorizedAccessException;
import com.musicmy.repository.TipousuarioRepository;
import com.musicmy.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements ServiceInterface<UsuarioEntity> {

    @Autowired
    private UsuarioRepository oUsuarioRepository;

    @Autowired
    private RandomService oRandomService;

    @Autowired
    private TipousuarioService oTipousuarioService;

    @Autowired
    private TipousuarioRepository oTipousuarioRepository;

    @Autowired
    private AuthService oAuthService;

    @Override
    public Long baseCreate() {
        try {
            // Leer el JSON desde resources/json/usuarios.json
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ClassPathResource resource = new ClassPathResource("json/usuario.json");
            List<UsuarioEntity> usuarios = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
            });

            reiniciarAutoIncrement();

            for (UsuarioEntity usuario : usuarios) {
                // TODO si da problemas el fill revisar si es por esto
                // usuario.setTipousuario(oTipousuarioService.get(usuario.getTipousuario().getId()));

                // Cargar imagen desde resources/img si existe
                byte[] imagen = cargarImagenDesdeResources("img/usuario.webp");
                if (imagen != null) {
                    usuario.setImg(imagen);
                }

                // Guardar en la base de datos
                oUsuarioRepository.save(usuario);
            }
            return oUsuarioRepository.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private byte[] cargarImagenDesdeResources(String ruta) {
        try {
            ClassPathResource resource = new ClassPathResource(ruta);
            Path path = resource.getFile().toPath();
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen: " + ruta);
            return null;
        }
    }

    @Transactional
    public void reiniciarAutoIncrement() {
        oUsuarioRepository.flush(); // Asegurar que los cambios han sido guardados
        oUsuarioRepository.resetAutoIncrement();

    }

    @Override
    public UsuarioEntity randomSelection() {
        if (oAuthService.isAdministrador()) {
            return oUsuarioRepository.findAll()
                    .get(oRandomService.getRandomInt(0, (int) oUsuarioRepository.count() - 1));
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public Page<UsuarioEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (oAuthService.isAdministrador()) {
            if (filter.isPresent()) {
                // TODO
                return oUsuarioRepository
                        .findByNombreContainingOrEmailContainingOrWebsiteContaining(
                                filter.get(), filter.get(), filter.get(), oPageable);
            } else {
                return oUsuarioRepository.findAll(oPageable);
            }
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public UsuarioEntity get(Long id) {
        if (oAuthService.isAdministrador() || oAuthService.isOneSelf(id)) {
            return oUsuarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario con el id " + id + " no encontrado"));
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    //TODO username
    public UsuarioEntity getByEmail(String email) {
        UsuarioEntity usuario = oUsuarioRepository.findByEmail(email).get();
        if (oAuthService.isAdministrador()) {
            return usuario;
        } else if (oAuthService.isOneSelf(usuario.getId())) {
            return usuario;
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

      public byte[] getImgById(Long id) {
        // UsuarioEntity usuario =  get(id);
        UsuarioEntity usuario =  oUsuarioRepository.findById(id).get();

        return usuario.getImg();
    }

    @Override
    public Long count() {
        return oUsuarioRepository.count();
    }

    @Override
    public Long delete(Long id) {
        UsuarioEntity usurio = oUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con el id " + id + " no encontrado"));

        if (oAuthService.isAdministrador()) {
            oUsuarioRepository.delete(usurio);
            return 1L;
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public UsuarioEntity create(UsuarioEntity oUsuarioEntity) {
        return Optional.ofNullable(oAuthService.isAdministrador() ? oUsuarioRepository.save(oUsuarioEntity) : null)
                .orElseThrow(() -> new UnauthorizedAccessException("Acceso denegado"));

    }

    public UsuarioEntity register(UsuarioEntity oUsuarioEntity) {
        oUsuarioEntity
                .setTipousuario(oTipousuarioService.get(oTipousuarioRepository.findByNombre("Usuario").get().getId()));
        return oUsuarioRepository.save(oUsuarioEntity);
    }

    @Override
    public UsuarioEntity update(UsuarioEntity oUsuarioEntity) {
        if (oAuthService.isOneSelf(oUsuarioEntity.getId()) || oAuthService.isAdministrador()) {
            UsuarioEntity oUsuarioEntityFromDatabase = oUsuarioRepository.findById(oUsuarioEntity.getId()).get();
            if (oUsuarioEntity.getUsername() != null) {
                oUsuarioEntityFromDatabase.setUsername(oUsuarioEntity.getUsername());
            }
            if (oUsuarioEntity.getNombre() != null) {
                oUsuarioEntityFromDatabase.setNombre(oUsuarioEntity.getNombre());
            }
            if (oUsuarioEntity.getFecha() != null) {
                oUsuarioEntityFromDatabase.setFecha(oUsuarioEntity.getFecha());
            }
            if (oUsuarioEntity.getDescripcion() != null) {
                oUsuarioEntityFromDatabase.setDescripcion(oUsuarioEntity.getDescripcion());
            }

            if (oAuthService.isAdministrador()) {
                if (oUsuarioEntity.getPassword() != null) {
                    oUsuarioEntityFromDatabase.setPassword(oUsuarioEntity.getPassword());
                }

                if (oUsuarioEntity.getEmail() != null) {
                    oUsuarioEntityFromDatabase.setEmail(oUsuarioEntity.getEmail());
                }
            }
            if (oUsuarioEntity.getWebsite() != null) {
                oUsuarioEntityFromDatabase.setWebsite(oUsuarioEntity.getWebsite());
            }

            if (oUsuarioEntity.getImg() != null) {
                oUsuarioEntityFromDatabase.setImg(oUsuarioEntity.getImg());
            }
            if (oUsuarioEntity.getTipousuario() != null) {
                oUsuarioEntityFromDatabase.setTipousuario(oUsuarioEntity.getTipousuario());
            }

            return oUsuarioRepository.save(oUsuarioEntityFromDatabase);
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }

    }

    public Boolean changePassword(UsuarioEntity oUsuarioEntity) {
        if (oAuthService.isOneSelf(oUsuarioEntity.getId())) {
            UsuarioEntity oUsuarioEntityFromDatabase = oUsuarioRepository.findById(oUsuarioEntity.getId()).get();
            oUsuarioEntityFromDatabase.setPassword(oUsuarioEntity.getPassword());
            oUsuarioRepository.save(oUsuarioEntityFromDatabase);
            return true;
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public Long deleteAll() {
        // if (oAuthService.isAdministrador()) {
        oUsuarioRepository.deleteAll();
        return this.count();
        // } else {
        // throw new UnauthorizedAccessException("No autorizado");
        // }
    }

    public boolean checkIfEmailExists(String email) {
        return oUsuarioRepository.findByEmail(email).isPresent();
    }

    public boolean checkIfUsernameExists(String username) {
        return oUsuarioRepository.findByUsername(username).isPresent();
    }

}
