package com.musicmy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmy.entity.ResenyaEntity;
import com.musicmy.entity.ResenyaLikeEntity;
import com.musicmy.entity.UsuarioEntity;
import com.musicmy.repository.ResenyaLikeRepository;
import com.musicmy.repository.ResenyaRepository;
import com.musicmy.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResenyaLikeService {

    @Autowired
    private ResenyaLikeRepository likeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ResenyaRepository resenyaRepository;

    public boolean likeResenya(Long usuarioId, Long resenyaId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ResenyaEntity resenya = resenyaRepository.findById(resenyaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        if (likeRepository.findByUsuarioAndResenya(usuario, resenya).isPresent()) {
            return false; // Ya ha dado like
        }

        ResenyaLikeEntity like = new ResenyaLikeEntity();
        like.setUsuario(usuario);
        like.setResenya(resenya);
        likeRepository.save(like);
        return true;
    }

    @Transactional
    public boolean unlikeResenya(Long usuarioId, Long resenyaId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ResenyaEntity resenya = resenyaRepository.findById(resenyaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        likeRepository.deleteByUsuarioAndResenya(usuario, resenya);
        return true;
    }

    public long countLikes(Long resenyaId) {
        ResenyaEntity resenya = resenyaRepository.findById(resenyaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        return likeRepository.countByResenya(resenya);
    }

    public boolean hasUserLiked(Long usuarioId, Long resenyaId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("UsuarioEntity no encontrado"));
        ResenyaEntity resenya = resenyaRepository.findById(resenyaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        return likeRepository.findByUsuarioAndResenya(usuario, resenya).isPresent();
    }
}
