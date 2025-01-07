package com.musicmy.dto;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import com.musicmy.entity.AlbumEntity;

public class AlbumDTO {
     private Long id;
    private String nombre;
    private LocalDate fecha;
    private String genero;
    private String descripcion;
    private String discografica;
    private String imgBase64;
    private int grupoalbumartistas;

    public AlbumDTO() {
    }

    public AlbumDTO(Long id, String nombre, LocalDate fecha, String genero, String descripcion, String discografica, String imgBase64, int grupoalbumartistas) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.genero = genero;
        this.descripcion = descripcion;
        this.discografica = discografica;
        this.imgBase64 = imgBase64;
        this.grupoalbumartistas = grupoalbumartistas;

    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDiscografica() {
        return discografica;
    }

    public void setDiscografica(String discografica) {
        this.discografica = discografica;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public int getGrupoalbumartistas() {
        return grupoalbumartistas;
    }

    public void setGrupoalbumartistas(int grupoalbumartistas) {
        this.grupoalbumartistas = grupoalbumartistas;
    }

    

    
}
