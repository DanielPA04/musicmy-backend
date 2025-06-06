package com.musicmy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.musicmy.entity.AlbumEntity;
import com.musicmy.entity.ResenyaEntity;
import com.musicmy.entity.UsuarioEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResenyaRepository extends JpaRepository<ResenyaEntity, Long> {

    Page<ResenyaEntity> findByNotaContainingOrDescripcionContainingOrWebsiteContaining(String filter1, String filter2,
            String filter3, Pageable pageable);

    Optional<ResenyaEntity> findByAlbumAndUsuario(AlbumEntity album, UsuarioEntity usuario);

    Page<ResenyaEntity> findByUsuario(UsuarioEntity usuario, Pageable pageable);

    // Reseñas más recientes del usuario (ordenadas por fecha descendente)
    Page<ResenyaEntity> findByUsuarioOrderByFechaDesc(UsuarioEntity usuario, Pageable pageable);

    // Reseñas con mayor nota del usuario (ordenadas por nota descendente)
    Page<ResenyaEntity> findByUsuarioOrderByNotaDesc(UsuarioEntity usuario, Pageable pageable);

    Page<ResenyaEntity> findByAlbum(AlbumEntity album, Pageable pageable);

    @Query(value = """
            SELECT r.*
            FROM resenya r
            LEFT JOIN resenya_like rl
              ON r.id = rl.id_resenya
            WHERE r.idalbum = :albumId
            GROUP BY r.id, r.nota, r.descripcion, r.fecha, r.website, r.idalbum, r.idusuario
            ORDER BY COUNT(rl.id_resenya) DESC
            """, countQuery = """
            SELECT COUNT(*)
            FROM resenya
            WHERE idalbum = :albumId
            """, nativeQuery = true)
    Page<ResenyaEntity> findByAlbumOrderByLikesDesc(
            @Param("albumId") Long albumId,
            Pageable pageable);

    @Query("""
            SELECT r
            FROM ResenyaEntity r
            JOIN r.usuario u
            WHERE r.album.id   = :albumId
              AND u.email      = :email
            """)
    Optional<ResenyaEntity> findByAlbumIdAndUsuarioEmail(
            @Param("albumId") Long albumId,
            @Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE resenya AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

}
