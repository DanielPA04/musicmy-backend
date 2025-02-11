package com.musicmy.repository;

import com.musicmy.entity.AlbumEntity;
import com.musicmy.entity.ArtistaEntity;
import com.musicmy.entity.GrupoalbumartistaEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoalbumartistaRepository extends JpaRepository<GrupoalbumartistaEntity, Long> {
   
    
    @Transactional
    void deleteByArtista(ArtistaEntity artista);

    @Transactional
    void deleteByAlbum(AlbumEntity album);
}
