package com.musicmy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.musicmy.entity.ResenyaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResenyaRepository extends JpaRepository<ResenyaEntity, Long> {

    Page<ResenyaEntity> findByNotaContainingOrDescripcionContainingOrWebsiteContaining(String filter1, String filter2,
            String filter3, Pageable pageable);

}
