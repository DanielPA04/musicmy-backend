package com.musicmy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.musicmy.entity.AlbumEntity;
import com.musicmy.entity.ResenyaEntity;
import com.musicmy.entity.UsuarioEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResenyaRepository extends JpaRepository<ResenyaEntity, Long> {

    Page<ResenyaEntity> findByNotaContainingOrDescripcionContainingOrWebsiteContaining(String filter1, String filter2,
            String filter3, Pageable pageable);

    Optional<ResenyaEntity> findByAlbumAndUsuario(AlbumEntity album, UsuarioEntity usuario);

    Page<ResenyaEntity> findByUsuario(UsuarioEntity usuario, Pageable pageable);
    

}
