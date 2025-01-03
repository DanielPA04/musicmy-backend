package com.musicmy.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.musicmy.entity.GrupoalbumartistaEntity;
import com.musicmy.repository.GrupoalbumartistaRepository;

import java.util.Optional;

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
            oGrupoalbumartistaEntityFromDatabase.setArtista(oArtistaService.get(oGrupoalbumartista.getArtista().getId()));
        }

        return oGrupoalbumartistaRepository.save(oGrupoalbumartistaEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oGrupoalbumartistaRepository.deleteAll();
        return this.count();
    }

}
