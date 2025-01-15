package com.musicmy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.musicmy.entity.TipousuarioEntity;


public interface TipousuarioRepository extends JpaRepository<TipousuarioEntity, Long> {
    
}
