package com.musicmy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicmy.entity.UsuarioverfEntity;

public interface UsuarioverfRepository extends JpaRepository<UsuarioverfEntity, Long> {

    Optional<UsuarioverfEntity> findByEmail(String email);

    Optional<UsuarioverfEntity> findByCode(String code);

    void deleteByEmail(String email);



    
}
