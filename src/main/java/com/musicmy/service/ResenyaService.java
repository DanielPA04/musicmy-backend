package com.musicmy.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musicmy.dto.ResenyaWithLikeCountDTO;
import com.musicmy.entity.ResenyaEntity;
import com.musicmy.entity.UsuarioEntity;
import com.musicmy.repository.ResenyaLikeRepository;
import com.musicmy.repository.ResenyaRepository;

import jakarta.transaction.Transactional;

@Service
public class ResenyaService implements ServiceInterface<ResenyaEntity> {

    @Autowired
    private ResenyaRepository oResenyaRepository;

    @Autowired
    private UsuarioService oUsuarioService;

    @Autowired
    private AlbumService oAlbumService;

    @Autowired
    private RandomService oRandomService;

    @Autowired
    private ResenyaLikeRepository oResenyaLikeRepository;

    @Override
    public Long baseCreate() {
        try {
            // Leer el JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ClassPathResource resource = new ClassPathResource("json/resenya.json");
            List<ResenyaEntity> resenyas = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
            });

            reiniciarAutoIncrement();

            for (ResenyaEntity resenya : resenyas) {

                // Guardar en la base de datos
                oResenyaRepository.save(resenya);
            }
            return oResenyaRepository.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Transactional
    public void reiniciarAutoIncrement() {
        oResenyaRepository.flush(); // Asegurar que los cambios han sido guardados
        oResenyaRepository.resetAutoIncrement();

    }

    @Override
    public ResenyaEntity randomSelection() {
        return oResenyaRepository.findAll().get(oRandomService.getRandomInt(0, (int) oResenyaRepository.count() - 1));
    }

    @Override
    public Page<ResenyaEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oResenyaRepository
                    .findByNotaContainingOrDescripcionContainingOrWebsiteContaining(
                            filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oResenyaRepository.findAll(oPageable);
        }
    }

    public Page<ResenyaEntity> getPageByUsuario(Long id, Pageable oPageable) {
        return oResenyaRepository.findByUsuario(oUsuarioService.get(id), oPageable);
    }

    public Page<ResenyaEntity> getResenyasRecientesByUsuario(Long idUsuario, Pageable pageable) {
        UsuarioEntity usuario = oUsuarioService.get(idUsuario);
        return oResenyaRepository.findByUsuarioOrderByFechaDesc(usuario, pageable);
    }

    public Page<ResenyaEntity> getResenyasTopByUsuario(Long idUsuario, Pageable pageable) {
        UsuarioEntity usuario = oUsuarioService.get(idUsuario);
        return oResenyaRepository.findByUsuarioOrderByNotaDesc(usuario, pageable);
    }

    public Page<ResenyaEntity> getPageByAlbum(Long id, Pageable oPageable) {
        return oResenyaRepository.findByAlbum(oAlbumService.get(id), oPageable);
    }

    @Override
    public ResenyaEntity get(Long id) {
        return oResenyaRepository.findById(id).get();
    }

    @Override
    public Long count() {
        return oResenyaRepository.count();
    }

    @Override
    public Long delete(Long id) {
        oResenyaRepository.deleteById(id);
        return 1L;
    }

    @Override
    public ResenyaEntity create(ResenyaEntity oResenyaEntity) {
        oResenyaEntity.setAlbum(oAlbumService.get(oResenyaEntity.getAlbum().getId()));
        oResenyaEntity.setUsuario(oUsuarioService.get(oResenyaEntity.getUsuario().getId()));
        oResenyaEntity.setFecha(LocalDate.now());
        return oResenyaRepository.save(oResenyaEntity);
    }

    @Override
    public ResenyaEntity update(ResenyaEntity oResenyaEntity) {
        ResenyaEntity oResenyaEntityFromDatabase = oResenyaRepository.findById(oResenyaEntity.getId()).get();
        if (oResenyaEntity.getNota() != null) {
            oResenyaEntityFromDatabase.setNota(oResenyaEntity.getNota());
        }
        if (oResenyaEntity.getDescripcion() != null) {
            oResenyaEntityFromDatabase.setDescripcion(oResenyaEntity.getDescripcion());
        }
        if (oResenyaEntity.getFecha() != null) {
            oResenyaEntityFromDatabase.setFecha(oResenyaEntity.getFecha());
        }
        if (oResenyaEntity.getWebsite() != null) {
            oResenyaEntityFromDatabase.setWebsite(oResenyaEntity.getWebsite());
        }
        if (oResenyaEntity.getAlbum() != null) {
            oResenyaEntityFromDatabase.setAlbum(oAlbumService.get(oResenyaEntity.getAlbum().getId()));
        }
        if (oResenyaEntity.getUsuario() != null) {
            oResenyaEntityFromDatabase.setUsuario(oUsuarioService.get(oResenyaEntity.getUsuario().getId()));
        }
        return oResenyaRepository.save(oResenyaEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oResenyaRepository.deleteAll();
        return this.count();
    }

    public boolean isResenyaAlreadyExists(ResenyaEntity oResenyaEntity) {
        return oResenyaRepository.findByAlbumAndUsuario(oResenyaEntity.getAlbum(), oResenyaEntity.getUsuario())
                .isPresent();
    }

    public boolean isResenyaAlreadyExists(String email, Long idAlbum) {
        return oResenyaRepository.findByAlbumAndUsuario(oAlbumService.get(idAlbum), oUsuarioService.getByEmail(email))
                .isPresent();
    }

    public ResenyaWithLikeCountDTO toDtoWithLikeCount(ResenyaEntity resenya) {
        ResenyaWithLikeCountDTO dto = new ResenyaWithLikeCountDTO();
        dto.setId(resenya.getId());
        dto.setNota(resenya.getNota());
        dto.setDescripcion(resenya.getDescripcion());
        dto.setFecha(resenya.getFecha());
        dto.setWebsite(resenya.getWebsite());
        dto.setAlbum(resenya.getAlbum());
        dto.setUsuario(resenya.getUsuario());
        dto.setLikeCount(oResenyaLikeRepository.countByResenya(resenya));
        return dto;
    }

}
