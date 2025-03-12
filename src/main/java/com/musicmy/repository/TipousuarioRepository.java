package com.musicmy.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.musicmy.entity.TipousuarioEntity;

import jakarta.transaction.Transactional;

public interface TipousuarioRepository extends JpaRepository<TipousuarioEntity, Long> {

     Page<TipousuarioEntity> findByNombreContaining(String filter1, Pageable pageable);

     Optional<TipousuarioEntity> findByNombre(String nombre);

     @Modifying
     @Transactional
     @Query(value = "ALTER TABLE tipousuario AUTO_INCREMENT = 1", nativeQuery = true)
     void resetAutoIncrement();
}
