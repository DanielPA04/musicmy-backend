package com.musicmy.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarioverf")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioverfEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String code;

    LocalDate exp;

    public UsuarioverfEntity(String email, String code, LocalDate exp) {
        this.email = email;
        this.code = code;
        this.exp = exp;
    }



}
