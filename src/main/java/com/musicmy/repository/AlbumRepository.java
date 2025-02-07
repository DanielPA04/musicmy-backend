package com.musicmy.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.musicmy.entity.AlbumEntity;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    Page<AlbumEntity> findByNombreContainingOrGeneroContainingOrDescripcionContainingOrDiscograficaContaining(
            String filter1, String filter2, String filter3, String filter4, Pageable oPageable);

    @Query(value = "SELECT AVG(nota) FROM resenya WHERE idAlbum = :id", nativeQuery = true)
    Optional<Double> getNotaMedia(Long id);


}
