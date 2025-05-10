package com.musicmy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.musicmy.entity.UsuarioEntity;

import jakarta.transaction.Transactional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByUsername(String username);

    Optional<UsuarioEntity> findByEmailAndPassword(String email, String password);

    Optional<UsuarioEntity> findByEmailOrUsernameAndPassword(String email, String username, String password);

    @Query("SELECT u FROM UsuarioEntity u WHERE (u.email = :identifier OR u.username = :identifier) AND u.password = :password")
    Optional<UsuarioEntity> findByIdentifierAndPassword(@Param("identifier") String identifier,
            @Param("password") String password);

    Optional<UsuarioEntity> findByEmailOrUsername(String email, String username);

    Page<UsuarioEntity> findByNombreContainingOrEmailContainingOrWebsiteContaining(
            String filter1, String filter2, String filter3, Pageable oPageable);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE usuario AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

}
