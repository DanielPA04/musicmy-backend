package com.musicmy.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.musicmy.entity.AlbumEntity;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    Page<AlbumEntity> findByNombreContainingOrGeneroContainingOrDescripcionContainingOrDiscograficaContaining(
            String filter1, String filter2, String filter3, String filter4, Pageable oPageable);


}
