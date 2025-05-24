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

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AlbumRepository
    extends JpaRepository<AlbumEntity, Long>,
    JpaSpecificationExecutor<AlbumEntity> {
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

 

@Query("""
      SELECT a
      FROM AlbumEntity a
      LEFT JOIN a.resenyas r
      WHERE (:genero IS NULL OR LOWER(a.genero) LIKE %:genero%) 
        AND (:discografica IS NULL OR LOWER(a.discografica) LIKE %:discografica%)
        AND (:nombre IS NULL OR LOWER(a.nombre) LIKE %:nombre%)
      GROUP BY a.id
      HAVING COUNT(r.id) > 0
      ORDER BY AVG(r.nota) DESC, COUNT(r.id) DESC
    """)
    Page<AlbumEntity> findFilteredTopRated(
      @Param("genero") String genero,
      @Param("discografica") String discografica,
      @Param("nombre") String nombre,
      Pageable pageable
    );

    @Query("""
      SELECT a
      FROM AlbumEntity a
      LEFT JOIN a.resenyas r
      WHERE r.fecha >= :sinceDate
        AND (:genero IS NULL OR LOWER(a.genero) LIKE %:genero%) 
        AND (:discografica IS NULL OR LOWER(a.discografica) LIKE %:discografica%)
        AND (:nombre IS NULL OR LOWER(a.nombre) LIKE %:nombre%)
      GROUP BY a.id
      ORDER BY COUNT(r.id) DESC
    """)
    Page<AlbumEntity> findFilteredPopularSince(
      @Param("sinceDate") LocalDate sinceDate,
      @Param("genero") String genero,
      @Param("discografica") String discografica,
      @Param("nombre") String nombre,
      Pageable pageable
    );


  @Modifying
  @Transactional
  @Query(value = "ALTER TABLE album AUTO_INCREMENT = 1", nativeQuery = true)
  void resetAutoIncrement();

}
