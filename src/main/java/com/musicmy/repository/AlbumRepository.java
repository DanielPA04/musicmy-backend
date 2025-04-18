package com.musicmy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.musicmy.entity.AlbumEntity;

import jakarta.transaction.Transactional;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    Page<AlbumEntity> findByNombreContainingOrGeneroContainingOrDescripcionContainingOrDiscograficaContaining(
            String filter1, String filter2, String filter3, String filter4, Pageable oPageable);

    @Query(value = "SELECT AVG(nota) FROM resenya WHERE idAlbum = :id", nativeQuery = true)
    Optional<Double> getNotaMedia(Long id);

    @Query(value = "SELECT al.* FROM album al JOIN grupoalbumartista gaa ON al.id = gaa.idAlbum JOIN artista a ON gaa.idArtista = a.id WHERE a.id = :id", nativeQuery = true)
    Optional<List<AlbumEntity>> findByArtistaId(Long id);

    Page<AlbumEntity> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE album AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

}
