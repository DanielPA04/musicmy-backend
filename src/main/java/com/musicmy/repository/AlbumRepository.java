package com.musicmy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Page<AlbumEntity> findAllByOrderByFechaDesc(Pageable pageable);

    // Método para álbumes con más reseñas (populares)
    @Query("SELECT a FROM AlbumEntity a LEFT JOIN a.resenyas r GROUP BY a.id ORDER BY COUNT(r.id) DESC")
    Page<AlbumEntity> findAllByOrderByResenyaCountDesc(Pageable pageable);

    // Método para álbumes populares desde cierta fecha
    @Query("SELECT a FROM AlbumEntity a LEFT JOIN a.resenyas r WHERE r.fecha >= :sinceDate GROUP BY a.id ORDER BY COUNT(r.id) DESC")
    Page<AlbumEntity> findAllPopularSince(@Param("sinceDate") LocalDate sinceDate, Pageable pageable);

    // Álbumes mejor valorados (mayor nota promedio)
  @Query("SELECT a FROM AlbumEntity a " +
           "LEFT JOIN a.resenyas r " +
           "GROUP BY a.id " +
           "HAVING COUNT(r.id) > 0 " +  // Solo álbumes con al menos 1 reseña cambiar a mas si creciera
           "ORDER BY AVG(r.nota) DESC, COUNT(r.id) DESC")  // Primero por nota, luego por cantidad de reseñas
    Page<AlbumEntity> findAllByOrderByAverageRatingDesc(Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE album AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

}
