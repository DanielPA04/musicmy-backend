package com.musicmy.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicmy.entity.UsuarioEntity;
import com.musicmy.repository.UsuarioRepository;

@Service
public class UsuarioService implements ServiceInterface<UsuarioEntity> {

    @Autowired
    private UsuarioRepository oUsuarioRepository;

    @Autowired
    private RandomService oRandomService;

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
            oUsuarioEntity.setEmail(emails[oRandomService.getRandomInt(0, emails.length - 1)]);
            oUsuarioEntity.setDescripcion(descripciones[oRandomService.getRandomInt(0, descripciones.length - 1)]);
            oUsuarioEntity.setWebsite(websites[oRandomService.getRandomInt(0, websites.length - 1)]);
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
        return oUsuarioRepository.findById(id).get();
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

}
