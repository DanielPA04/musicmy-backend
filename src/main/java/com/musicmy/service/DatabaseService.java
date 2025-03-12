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

    @Autowired
    private UsuarioService oUsuarioService;

    @Autowired
    private ResenyaService oResenyaService;
    
    @Autowired
    private TipousuarioService oTipousuarioService;
   

    public Long fill() {
        oAlbumService.baseCreate();
        oArtistaService.baseCreate();
        oGrupoalbumartistaService.baseCreate();
        oTipousuarioService.baseCreate();
        oUsuarioService.baseCreate();
        oResenyaService.baseCreate();
        return 0L;
    }

    public Long deleteAll(){
        oGrupoalbumartistaService.deleteAll();
        oResenyaService.deleteAll();
        oAlbumService.deleteAll();
        oArtistaService.deleteAll();
        oUsuarioService.deleteAll();
        oTipousuarioService.deleteAll();
        return 0L;
    }

}