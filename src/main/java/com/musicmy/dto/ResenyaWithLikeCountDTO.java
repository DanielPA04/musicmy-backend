package com.musicmy.dto;
import java.time.LocalDate;

import com.musicmy.entity.AlbumEntity;
import com.musicmy.entity.UsuarioEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResenyaWithLikeCountDTO {
    private Long id;
    private Integer nota;
    private String descripcion;
    private LocalDate fecha;
    private String website;
    private AlbumEntity album;
    private UsuarioEntity usuario;
    private Long likeCount;

    // Getters y Setters
}

