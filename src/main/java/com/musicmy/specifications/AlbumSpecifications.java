package com.musicmy.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.musicmy.entity.AlbumEntity;

public class AlbumSpecifications {

    public static Specification<AlbumEntity> hasGenero(String genero) {
        return (root, query, cb) -> {
            if (genero == null || genero.isBlank()) {
                return cb.conjunction();
            }
            // Lower + LIKE %valor% para buscar substrings case-insensitive
            return cb.like(
                    cb.lower(root.get("genero")),
                    "%" + genero.trim().toLowerCase() + "%");
        };
    }

    public static Specification<AlbumEntity> hasDiscografica(String discografica) {
        return (root, query, cb) -> {
            if (discografica == null || discografica.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("discografica")),
                    "%" + discografica.trim().toLowerCase() + "%");
        };
    }

    public static Specification<AlbumEntity> nombreContains(String nombre) {
        return (root, query, cb) -> nombre == null ? null
                : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
    }

    // Combinamos todas en una sola spec
    public static Specification<AlbumEntity> filter(
            String genero,
            String discografica,
            String nombre) {
        return Specification
                .where(hasGenero(genero))
                .and(hasDiscografica(discografica))
                .and(nombreContains(nombre));
    }
}
