package com.musicmy.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;

@JsonPropertyOrder({ "id", "nombre", "nombreReal", "descripcion", "spotify", "img", "grupoalbumartistas" })
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "artista")
public class ArtistaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    
    private String nombrereal;

    @Size(min = 3, max = 500)
    private String descripcion;

    private String spotify;

    @Lob
    @JsonIgnore
    private byte[] img;

    @OneToMany(mappedBy = "artista", fetch = FetchType.LAZY)
    private java.util.List<GrupoalbumartistaEntity> grupoalbumartistas;

    public ArtistaEntity() {
        this.grupoalbumartistas = new java.util.ArrayList<>();
    }

    public ArtistaEntity(String nombre, String nombrereal, String descripcion, String spotify, byte[] img) {
        this.nombre = nombre;
        this.nombrereal = nombrereal;
        this.descripcion = descripcion;
        this.spotify = spotify;
        this.img = img;
        this.grupoalbumartistas = new java.util.ArrayList<>();
    }

    public int getGrupoalbumartistas() {
        return this.grupoalbumartistas.size();
    }

}
