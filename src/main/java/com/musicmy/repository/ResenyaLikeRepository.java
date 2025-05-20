package com.musicmy.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.musicmy.entity.ResenyaEntity;
import com.musicmy.entity.ResenyaLikeEntity;
import com.musicmy.entity.UsuarioEntity;

public interface ResenyaLikeRepository extends JpaRepository<ResenyaLikeEntity, Long> {
    Optional<ResenyaLikeEntity> findByUsuarioAndResenya(UsuarioEntity usuario, ResenyaEntity resenya);
    long countByResenya(ResenyaEntity resenya);
    void deleteByUsuarioAndResenya(UsuarioEntity usuario, ResenyaEntity resenya);
}
