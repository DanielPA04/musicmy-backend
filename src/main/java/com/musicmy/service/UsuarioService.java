package com.musicmy.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.musicmy.entity.UsuarioEntity;
import com.musicmy.exception.ResourceNotFoundException;
import com.musicmy.exception.UnauthorizedAccessException;
import com.musicmy.repository.TipousuarioRepository;
import com.musicmy.repository.UsuarioRepository;

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

    String[] nombres = { "Juan", "María", "Carlos", "Ana" };
    String[] fechas = { "2000-01-01", "2000-01-02", "2000-01-03", "2000-01-04" };
    String[] descripciones = {
            "Estudiante de informática.",
            "Diseñadora gráfica.",
            "Ingeniero de software.",
            "Profesora de matemáticas."
    };
    String[] emails = { "juan@example.com", "maria@example.com", "carlos@example.com", "ana@example.com" };
    String[] websites = { "https://juan.com", "https://maria.com", "https://carlos.com", "https://ana.com" };

    @Override
    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            UsuarioEntity oUsuarioEntity = new UsuarioEntity();
            oUsuarioEntity.setNombre(nombres[oRandomService.getRandomInt(0, nombres.length - 1)]);
            oUsuarioEntity.setFecha(LocalDate.parse(fechas[oRandomService.getRandomInt(0, fechas.length - 1)]));
            oUsuarioEntity
                    .setEmail(emails[oRandomService.getRandomInt(0, emails.length - 1)] + UUID.randomUUID().toString());
            oUsuarioEntity.setPassword("10a28e5c8e725bcf4454d93456000b2d9d934f3c816525fabf9a6106f676556f");
            oUsuarioEntity.setDescripcion(descripciones[oRandomService.getRandomInt(0, descripciones.length - 1)]);
            oUsuarioEntity.setWebsite(websites[oRandomService.getRandomInt(0, websites.length - 1)]);
            oUsuarioEntity.setTipousuario(oTipousuarioService
                    .get((long) oRandomService.getRandomInt(0, (int) (oTipousuarioService.count() - 1))));
            oUsuarioRepository.save(oUsuarioEntity);
        }
        return oUsuarioRepository.count();
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
            return oUsuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario con el id " + id + " no encontrado"));
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

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
        if (oAuthService.isAdministrador()) {
            oUsuarioRepository.deleteAll();
            return this.count();
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    public boolean checkIfEmailExists(String email) {
        return oUsuarioRepository.findByEmail(email).isPresent();
    }

}
