package com.musicmy.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.musicmy.entity.ResenyaEntity;
import com.musicmy.repository.ResenyaRepository;

@Service
public class ResenyaService implements ServiceInterface<ResenyaEntity> {

    @Autowired
    private ResenyaRepository oResenyaRepository;

    @Autowired
    private UsuarioService oUsuarioService;

    @Autowired
    private AlbumService oAlbumService;

    @Autowired
    private RandomService oRandomService;

    String[] descripciones = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium.",
            "Totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.", };
    String[] websites = { "https://example.com", "https://randomsite.com", "https://mysite.org",
            "https://educationalplatform.net", "https://learnmore.edu" };

    Integer[] notas = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    LocalDate[] fechas = {
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 6, 15),
            LocalDate.of(2024, 12, 25),
            LocalDate.of(2025, 3, 20),
            LocalDate.of(2025, 7, 4)
    };

    String[] urls = {
            "https://www.google.com",
            "https://www.github.com",
            "https://www.stackoverflow.com",
            "https://www.oracle.com"
    };

    @Override
    public Long randomCreate(Long cantidad) {

        for (int i = 0; i < cantidad; i++) {
            ResenyaEntity oResenyaEntity = new ResenyaEntity();
            oResenyaEntity.setId(i + 1L);
            oResenyaEntity.setNota(notas[oRandomService.getRandomInt(0, notas.length - 1)]);
            oResenyaEntity.setDescripcion(descripciones[oRandomService.getRandomInt(0, descripciones.length - 1)]);
            oResenyaEntity.setFecha(fechas[oRandomService.getRandomInt(0, fechas.length - 1)]);
            oResenyaEntity.setWebsite(urls[oRandomService.getRandomInt(0, urls.length - 1)]);

            oResenyaEntity.setAlbum(oAlbumService.randomSelection());

            oResenyaEntity.setUsuario(oUsuarioService.randomSelection());

            oResenyaRepository.save(oResenyaEntity);

        }

        return oResenyaRepository.count();
    }

    @Override
    public ResenyaEntity randomSelection() {
        return oResenyaRepository.findAll().get(oRandomService.getRandomInt(0, (int) oResenyaRepository.count() - 1));
    }

    @Override
    public Page<ResenyaEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oResenyaRepository
                    .findByNotaContainingOrDescripcionContainingOrWebsiteContaining(
                            filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oResenyaRepository.findAll(oPageable);
        }
    }

    @Override
    public ResenyaEntity get(Long id) {
        return oResenyaRepository.findById(id).get();
    }

    @Override
    public Long count() {
        return oResenyaRepository.count();
    }

    @Override
    public Long delete(Long id) {
        oResenyaRepository.deleteById(id);
        return 1L;
    }

    @Override
    public ResenyaEntity create(ResenyaEntity oResenyaEntity) {
        oResenyaEntity.setFecha(LocalDate.now());
        return oResenyaRepository.save(oResenyaEntity);
    }

    @Override
    public ResenyaEntity update(ResenyaEntity oResenyaEntity) {
        ResenyaEntity oResenyaEntityFromDatabase = oResenyaRepository.findById(oResenyaEntity.getId()).get();
        if (oResenyaEntity.getNota() != null) {
            oResenyaEntityFromDatabase.setNota(oResenyaEntity.getNota());
        }
        if (oResenyaEntity.getDescripcion() != null) {
            oResenyaEntityFromDatabase.setDescripcion(oResenyaEntity.getDescripcion());
        }
        if (oResenyaEntity.getFecha() != null) {
            oResenyaEntityFromDatabase.setFecha(oResenyaEntity.getFecha());
        }
        if (oResenyaEntity.getWebsite() != null) {
            oResenyaEntityFromDatabase.setWebsite(oResenyaEntity.getWebsite());
        }
        if (oResenyaEntity.getAlbum() != null) {
            oResenyaEntityFromDatabase.setAlbum(oAlbumService.get(oResenyaEntity.getAlbum().getId()));
        }
        if (oResenyaEntity.getUsuario() != null) {
            oResenyaEntityFromDatabase.setUsuario(oUsuarioService.get(oResenyaEntity.getUsuario().getId()));
        }
        return oResenyaRepository.save(oResenyaEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oResenyaRepository.deleteAll();
        return this.count();
    }

}
