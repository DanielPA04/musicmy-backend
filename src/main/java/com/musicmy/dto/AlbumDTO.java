package com.musicmy.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {
    private Long id;
    private String nombre;
    private LocalDate fecha;
    private String genero;
    private String descripcion;
    private String discografica;
    private String imgBase64;
    private int grupoalbumartistas;
    private int resenyas;

}
