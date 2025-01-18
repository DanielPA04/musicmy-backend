package com.musicmy.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.musicmy.entity.TipousuarioEntity;


public interface TipousuarioRepository extends JpaRepository<TipousuarioEntity, Long> {
    
     Page<TipousuarioEntity> findByNombreContaining(String filter1, Pageable pageable);

     Optional<TipousuarioEntity> findByNombre(String nombre);
}
