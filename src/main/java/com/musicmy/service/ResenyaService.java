package com.musicmy.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicmy.entity.AlbumEntity;
import com.musicmy.repository.ResenyaRepository;

@Service
public class ResenyaService implements ServiceInterface<AlbumEntity> {

    @Autowired
    private ResenyaRepository oResenyaRepository;

    @Autowired
    private RandomService oRandomService;

   

    @Override
    public Long randomCreate(Long cantidad) {
       
        return oResenyaRepository.count();
    }

    @Override
    public AlbumEntity randomSelection() {
        return oResenyaRepository.findAll().get(oRandomService.getRandomInt(0, (int) oResenyaRepository.count() - 1));
    }

    @Override
    public Page<AlbumEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oResenyaRepository
                    .findByNombreContainingOrGeneroContainingOrDescripcionContainingOrDiscograficaContaining(
                            filter.get(), filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oResenyaRepository.findAll(oPageable);
        }
    }

    @Override
    public AlbumEntity get(Long id) {
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
    public AlbumEntity create(AlbumEntity oAlbumEntity) {
        return oResenyaRepository.save(oAlbumEntity);
    }

    @Override
    public AlbumEntity update(AlbumEntity oAlbumEntity) {
        AlbumEntity oAlbumEntityFromDatabase = oResenyaRepository.findById(oAlbumEntity.getId()).get();
        if (oAlbumEntity.getNombre() != null) {
            oAlbumEntityFromDatabase.setNombre(oAlbumEntity.getNombre());
        }
        if (oAlbumEntity.getFecha() != null) {
            oAlbumEntityFromDatabase.setFecha(oAlbumEntity.getFecha());
        }
        if (oAlbumEntity.getGenero() != null) {
            oAlbumEntityFromDatabase.setGenero(oAlbumEntity.getGenero());
        }
        if (oAlbumEntity.getDescripcion() != null) {
            oAlbumEntityFromDatabase.setDescripcion(oAlbumEntity.getDescripcion());
        }
        if (oAlbumEntity.getDiscografica() != null) {
            oAlbumEntityFromDatabase.setDiscografica(oAlbumEntity.getDiscografica());
        }
        if (oAlbumEntity.getImg() != null) {
            oAlbumEntityFromDatabase.setImg(oAlbumEntity.getImg());
        }
        return oResenyaRepository.save(oAlbumEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oResenyaRepository.deleteAll();
        return this.count();
    }

}
