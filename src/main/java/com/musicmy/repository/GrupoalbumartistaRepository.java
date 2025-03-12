package com.musicmy.repository;

import com.musicmy.entity.AlbumEntity;
import com.musicmy.entity.ArtistaEntity;
import com.musicmy.entity.GrupoalbumartistaEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GrupoalbumartistaRepository extends JpaRepository<GrupoalbumartistaEntity, Long> {

    @Transactional
    void deleteByArtista(ArtistaEntity artista);

    @Transactional
    void deleteByAlbum(AlbumEntity album);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE grupoalbumartista AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
