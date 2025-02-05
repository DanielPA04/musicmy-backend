package com.musicmy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.musicmy.entity.ArtistaEntity;

public interface ArtistaRepository extends JpaRepository<ArtistaEntity, Long> {

    Page<ArtistaEntity> findByNombreContainingOrNombrerealContainingOrDescripcionContaining(
            String filter1, String filter2, String filter3, Pageable oPageable);

    @Query(value = "SELECT a.* FROM artista a JOIN grupoalbumartista gaa ON a.id = gaa.idArtista JOIN album al ON gaa.idAlbum = al.id WHERE al.id = :id", nativeQuery = true)
    Optional<List<ArtistaEntity>> findByAlbumId(Long id);
}
