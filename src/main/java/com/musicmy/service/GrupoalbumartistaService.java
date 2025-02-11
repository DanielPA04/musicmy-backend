package com.musicmy.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.musicmy.entity.AlbumEntity;
import com.musicmy.entity.ArtistaEntity;
import com.musicmy.entity.GrupoalbumartistaEntity;
import com.musicmy.repository.GrupoalbumartistaRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GrupoalbumartistaService implements ServiceInterface<GrupoalbumartistaEntity> {

    @Autowired
    private GrupoalbumartistaRepository oGrupoalbumartistaRepository;

    @Autowired
    private AlbumService oAlbumService;

    @Autowired
    private ArtistaService oArtistaService;

    @Autowired
    private RandomService oRandomService;

    @Override
    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            GrupoalbumartistaEntity oGrupoalbumartistaEntity = new GrupoalbumartistaEntity();
            oGrupoalbumartistaEntity.setAlbum(oAlbumService.randomSelection());
            oGrupoalbumartistaEntity.setArtista(oArtistaService.randomSelection());
            oGrupoalbumartistaRepository.save(oGrupoalbumartistaEntity);
        }
        return oGrupoalbumartistaRepository.count();

    }

    @Override
    public GrupoalbumartistaEntity randomSelection() {
        return oGrupoalbumartistaRepository.findAll()
                .get(oRandomService.getRandomInt(0, (int) oGrupoalbumartistaRepository.count() - 1));
    }

    @Override
    public Page<GrupoalbumartistaEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            throw new UnsupportedOperationException("Not supported");
        } else {
            return oGrupoalbumartistaRepository.findAll(oPageable);
        }
    }

    @Override
    public GrupoalbumartistaEntity get(Long id) {
        return oGrupoalbumartistaRepository.findById(id).get();
    }

    @Override
    public Long count() {
        return oGrupoalbumartistaRepository.count();
    }

    @Override
    public Long delete(Long id) {
        oGrupoalbumartistaRepository.deleteById(id);
        return 1L;
    }

    public Long deleteByArtista(Long id) {
        oGrupoalbumartistaRepository.deleteByArtista(oArtistaService.get(id));
        return 1L;
    }

    @Override
    public GrupoalbumartistaEntity create(GrupoalbumartistaEntity oGrupoalbumartistaEntity) {
        return oGrupoalbumartistaRepository.save(oGrupoalbumartistaEntity);
    }

    @Override
    public GrupoalbumartistaEntity update(GrupoalbumartistaEntity oGrupoalbumartista) {
        GrupoalbumartistaEntity oGrupoalbumartistaEntityFromDatabase = oGrupoalbumartistaRepository
                .findById(oGrupoalbumartista.getId()).get();

        if (oGrupoalbumartista.getAlbum() != null) {
            oGrupoalbumartistaEntityFromDatabase.setAlbum(oAlbumService.get(oGrupoalbumartista.getAlbum().getId()));
        }

        if (oGrupoalbumartista.getArtista() != null) {
            oGrupoalbumartistaEntityFromDatabase
                    .setArtista(oArtistaService.get(oGrupoalbumartista.getArtista().getId()));
        }

        return oGrupoalbumartistaRepository.save(oGrupoalbumartistaEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oGrupoalbumartistaRepository.deleteAll();
        return this.count();
    }


    public List<ArtistaEntity> updateArtistasToAlbum(List<ArtistaEntity> artistas, Long idAlbum) {
        AlbumEntity oAlbumEntity = oAlbumService.get(idAlbum);
        
        List<ArtistaEntity> oArtistasFromDatabase = oArtistaService.getByIdAlbum(idAlbum);
        Set<Long> artistasIdsFromDatabase = oArtistasFromDatabase.stream()
            .map(ArtistaEntity::getId)
            .collect(Collectors.toSet());
    
        Set<Long> artistasIds = artistas.stream()
            .map(ArtistaEntity::getId)
            .collect(Collectors.toSet());
    
        for (ArtistaEntity artista : artistas) {
            if (!artistasIdsFromDatabase.contains(artista.getId())) {
                this.create(new GrupoalbumartistaEntity(null, oAlbumEntity, artista));
            }
        }
    
        for (ArtistaEntity artista : oArtistasFromDatabase) {
            if (!artistasIds.contains(artista.getId())) {
                this.deleteByArtista(artista.getId());
            }
        }
        return oArtistaService.getByIdAlbum(idAlbum);
    }

    public List<ArtistaEntity> updateAlbumesToArtista(List<AlbumEntity> albumes, Long idArtista) {
        ArtistaEntity oArtistaEntity = oArtistaService.get(idArtista);
        
        List<AlbumEntity> oAlbumesFromDatabase = oAlbumService.getByIdArtista(idArtista);

        Set<Long> albumesIdsFromDatabase = oAlbumesFromDatabase.stream()
            .map(AlbumEntity::getId)
            .collect(Collectors.toSet());
    
        Set<Long> albumesIds = albumes.stream()
            .map(AlbumEntity::getId)
            .collect(Collectors.toSet());
    
        for (AlbumEntity album : albumes) {
            if (!albumesIdsFromDatabase.contains(album.getId())) {
                this.create(new GrupoalbumartistaEntity(null, album, oArtistaEntity));
            }
        }
    
        for (AlbumEntity album : oAlbumesFromDatabase) {
            if (!albumesIds.contains(album.getId())) {
                this.deleteByArtista(album.getId());
            }
        }
        return oArtistaService.getByIdAlbum(idArtista);
    }

}
