package com.musicmy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicmy.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    
}
