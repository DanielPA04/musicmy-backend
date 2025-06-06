package com.musicmy.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class UsuarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nombre;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    private String descripcion;

    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String website;

    @Lob
    @JsonIgnore
    private byte[] img;

    private String codverf;

    private String codresetpwd;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "idtipousuario")
    private TipousuarioEntity tipousuario;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private java.util.List<ResenyaEntity> resenyas;

    public UsuarioEntity() {
        this.resenyas = new java.util.ArrayList<>();
    }

    public UsuarioEntity(String username, String nombre, LocalDate fecha, String descripcion, String email,
            String password, String website, byte[] img, TipousuarioEntity tipousuario) {
        this.username = username;
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.email = email;
        this.password = password;
        this.website = website;
        this.img = img;
        this.tipousuario = tipousuario;
        this.resenyas = new java.util.ArrayList<>();
    }

    public UsuarioEntity(Long id, String username, String nombre, LocalDate fecha, String descripcion, String email,
            String password, String website, byte[] img, TipousuarioEntity tipousuario) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.email = email;
        this.password = password;
        this.website = website;
        this.img = img;
        this.tipousuario = tipousuario;
        this.resenyas = new java.util.ArrayList<>();
    }

    public int getResenyas() {
        return this.resenyas.size();
    }

}
