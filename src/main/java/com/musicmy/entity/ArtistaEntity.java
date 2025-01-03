package com.musicmy.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;

@JsonPropertyOrder({ "id", "nombre", "nombreReal", "descripcion", "spotify", "img", "grupoalbumartistas" })
@Entity
@Table(name = "artista")
public class ArtistaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 255)
    private String nombre;

    @Size(min = 3, max = 255)
    private String nombrereal;

    @Size(min = 3, max = 255)
    private String descripcion;

    private String spotify;

    private Blob img;

    @OneToMany(mappedBy = "artista", fetch = FetchType.LAZY)
    private java.util.List<GrupoalbumartistaEntity> grupoalbumartistas;

    public ArtistaEntity() {
        this.grupoalbumartistas = new java.util.ArrayList<>();
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

    public String getNombreReal() {
        return nombrereal;
    }

    public void setNombreReal(String nombrereal) {
        this.nombrereal = nombrereal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

    public Blob getImg() {
        return img;
    }

    public void setImg(Blob img) {
        this.img = img;
    }

    public int getGrupoalbumartistas() {
        return grupoalbumartistas.size();
    }

}
