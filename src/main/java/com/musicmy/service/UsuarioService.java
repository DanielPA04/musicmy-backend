package com.musicmy.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicmy.entity.UsuarioEntity;
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
        return oUsuarioRepository.findAll().get(oRandomService.getRandomInt(0, (int) oUsuarioRepository.count() - 1));
    }

    @Override
    public Page<UsuarioEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oUsuarioRepository
                    .findByNombreContainingOrEmailContainingOrWebsiteContaining(
                            filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oUsuarioRepository.findAll(oPageable);
        }
    }

    @Override
    public UsuarioEntity get(Long id) {
        if (oAuthService.isAdministrador()) {
            return oUsuarioRepository.findById(id).get();
        } else if (oAuthService.isOneSelf(id)) {
            return oUsuarioRepository.findById(id).get();
        } else {
            // TODO
            throw new UnsupportedOperationException("Not supported");
        }
    }

    public UsuarioEntity getByEmail(String email) {
        UsuarioEntity usuario = oUsuarioRepository.findByEmail(email).get();
        if (oAuthService.isAdministrador()) {
            return usuario;
        } else if (oAuthService.isOneSelf(usuario.getId())) {
            return usuario;
        } else {
            // TODO
            throw new UnsupportedOperationException("Not supported");
        }
    }

    @Override
    public Long count() {
        return oUsuarioRepository.count();
    }

    @Override
    public Long delete(Long id) {
        oUsuarioRepository.deleteById(id);
        return 1L;
    }

    @Override
    public UsuarioEntity create(UsuarioEntity oUsuarioEntity) {
        //TODO
       
        return Optional.ofNullable(oAuthService.isAdministrador() ? oUsuarioRepository.save(oUsuarioEntity) : null)
        .orElseThrow(() -> new RuntimeException("Acceso denegado"));

    }

    public UsuarioEntity register(UsuarioEntity oUsuarioEntity) {
        oUsuarioEntity.setTipousuario(oTipousuarioService.get(oTipousuarioRepository.findByNombre("Usuario").get().getId()));
        return oUsuarioRepository.save(oUsuarioEntity);
    }


    @Override
    public UsuarioEntity update(UsuarioEntity oUsuarioEntity) {
        UsuarioEntity oUsuarioEntityFromDatabase = oUsuarioRepository.findById(oUsuarioEntity.getId()).get();
        if (oUsuarioEntity.getNombre() != null) {
            oUsuarioEntityFromDatabase.setNombre(oUsuarioEntity.getNombre());
        }

        return oUsuarioRepository.save(oUsuarioEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oUsuarioRepository.deleteAll();
        return this.count();
    }

    public boolean checkIfEmailExists(String email){
        return oUsuarioRepository.findByEmail(email).isPresent();
    }

}
