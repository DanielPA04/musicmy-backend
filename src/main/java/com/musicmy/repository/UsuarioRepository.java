package com.musicmy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.musicmy.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByEmailAndPassword(String email, String password);

    Page<UsuarioEntity> findByNombreContainingOrEmailContainingOrWebsiteContaining(
            String filter1, String filter2, String filter3, Pageable oPageable);


    
}
