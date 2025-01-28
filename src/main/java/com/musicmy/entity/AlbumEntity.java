package com.musicmy.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "album")
@Getter
@Setter
@AllArgsConstructor
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 255)
    private String nombre;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    private String genero;

    private String descripcion;

    private String discografica;

    @Lob
    @JsonIgnore
    private byte[] img;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private java.util.List<GrupoalbumartistaEntity> grupoalbumartistas;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private java.util.List<ResenyaEntity> resenyas;

    public AlbumEntity() {
        this.grupoalbumartistas = new java.util.ArrayList<>();
        this.resenyas = new java.util.ArrayList<>();
    }

    public int getGrupoalbumartistas() {
        return grupoalbumartistas.size();
    }

    public int getResenyas() {
        return resenyas.size();
    }

}
