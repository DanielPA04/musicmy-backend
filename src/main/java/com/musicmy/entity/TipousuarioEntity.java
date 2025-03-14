package com.musicmy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipousuario")
@AllArgsConstructor
@Getter
@Setter
public class TipousuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "tipousuario", fetch = FetchType.LAZY)
    private java.util.List<UsuarioEntity> usuarios;

    public TipousuarioEntity() {
        this.usuarios = new java.util.ArrayList<>();
    }

    public TipousuarioEntity(String nombre) {
        this.nombre = nombre;
    }

    public int getUsuarios() {
        return this.usuarios.size();
    }


    
}
