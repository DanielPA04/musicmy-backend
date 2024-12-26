package com.musicmy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.musicmy.entity.ArtistaEntity;

public interface ArtistaRepository extends JpaRepository<ArtistaEntity, Long> {

    Page<ArtistaEntity> findByNombreContainingOrNombreRealContainingOrDescripcionContaining(
            String filter1, String filter2, String filter3, Pageable oPageable);
}
