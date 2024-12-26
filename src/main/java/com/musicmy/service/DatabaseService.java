package com.musicmy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private ArtistaService oArtistaService;
    @Autowired
    private AlbumService oAlbumService;
    @Autowired
    private GrupoalbumartistaService oGrupoalbumartistaService;
   

    public Long fill() {
        oAlbumService.randomCreate(50L);
        oArtistaService.randomCreate(20L);
        oGrupoalbumartistaService.randomCreate(20L);
        return 0L;
    }

    public Long deleteAll(){
        oAlbumService.deleteAll();
        oArtistaService.deleteAll();
        oGrupoalbumartistaService.deleteAll();
        return 0L;
    }

}