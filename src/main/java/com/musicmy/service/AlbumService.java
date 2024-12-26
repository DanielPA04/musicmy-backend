package com.musicmy.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicmy.entity.AlbumEntity;
import com.musicmy.repository.AlbumRepository;

@Service
public class AlbumService implements ServiceInterface<AlbumEntity> {

    @Autowired
    private AlbumRepository oAlbumRepository;

    @Autowired
    private RandomService oRandomService;

    private String[] nombres = {
            "Album_1", "Album_2", "Album_3", "Album_4", "Album_5"
    };

    private LocalDate[] fechas = {
            LocalDate.of(1980, 5, 15),
            LocalDate.of(1990, 3, 22),
            LocalDate.of(2000, 11, 10),
            LocalDate.of(1975, 8, 5),
            LocalDate.of(1985, 12, 25)
    };

    private String[] generos = {
            "Pop", "Rock", "Jazz", "Hip-Hop", "Clásica"
    };

    private String[] descripciones = {
            "Un artista destacado en su género.",
            "Música revolucionaria e influyente.",
            "Estilo único y vanguardista.",
            "Creador de melodías inolvidables.",
            "Una leyenda en su industria."
    };

    private String[] discograficas = {
            "Sony Music", "Universal Music", "Warner Music", "EMI", "Independiente"
    };

    @Override
    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            AlbumEntity oAlbumEntity = new AlbumEntity();
            oAlbumEntity.setNombre(nombres[oRandomService.getRandomInt(0, nombres.length - 1)]);
            oAlbumEntity.setFecha(fechas[oRandomService.getRandomInt(0, fechas.length - 1)]);
            oAlbumEntity.setGenero(generos[oRandomService.getRandomInt(0, generos.length - 1)]);
            oAlbumEntity.setDescripcion(descripciones[oRandomService.getRandomInt(0, descripciones.length - 1)]);
            oAlbumEntity.setDiscografica(discograficas[oRandomService.getRandomInt(0, discograficas.length - 1)]);
            oAlbumEntity.setImg(null);
            oAlbumRepository.save(oAlbumEntity);
        }
        return oAlbumRepository.count();
    }

    @Override
    public AlbumEntity randomSelection() {
        return oAlbumRepository.findAll().get(oRandomService.getRandomInt(0, (int) oAlbumRepository.count() - 1));
    }

    @Override
    public Page<AlbumEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oAlbumRepository
                    .findByNombreContainingOrGeneroContainingOrDescripcionContainingOrDiscograficaContaining(
                            filter.get(), filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oAlbumRepository.findAll(oPageable);
        }
    }

    @Override
    public AlbumEntity get(Long id) {
        return oAlbumRepository.findById(id).get();
    }

    @Override
    public Long count() {
        return oAlbumRepository.count();
    }

    @Override
    public Long delete(Long id) {
        oAlbumRepository.deleteById(id);
        return 1L;
    }

    @Override
    public AlbumEntity create(AlbumEntity oUsuarioEntity) {
        return oAlbumRepository.save(oUsuarioEntity);
    }

    @Override
    public AlbumEntity update(AlbumEntity oUsuarioEntity) {
        AlbumEntity oAlbumEntityFromDatabase = oAlbumRepository.findById(oUsuarioEntity.getId()).get();
        if (oUsuarioEntity.getNombre() != null) {
            oAlbumEntityFromDatabase.setNombre(oUsuarioEntity.getNombre());
        }
        if (oUsuarioEntity.getFecha() != null) {
            oAlbumEntityFromDatabase.setFecha(oUsuarioEntity.getFecha());
        }
        if (oUsuarioEntity.getGenero() != null) {
            oAlbumEntityFromDatabase.setGenero(oUsuarioEntity.getGenero());
        }
        if (oUsuarioEntity.getDescripcion() != null) {
            oAlbumEntityFromDatabase.setDescripcion(oUsuarioEntity.getDescripcion());
        }
        if (oUsuarioEntity.getDiscografica() != null) {
            oAlbumEntityFromDatabase.setDiscografica(oUsuarioEntity.getDiscografica());
        }
        if (oUsuarioEntity.getImg() != null) {
            oAlbumEntityFromDatabase.setImg(oUsuarioEntity.getImg());
        }
        return oAlbumRepository.save(oAlbumEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oAlbumRepository.deleteAll();
        return this.count();
    }

}
