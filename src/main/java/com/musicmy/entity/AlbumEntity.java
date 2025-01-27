package com.musicmy.entity;

import java.time.LocalDate;
import java.util.Base64;
import java.io.InputStream;
import java.sql.Blob;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musicmy.dto.AlbumDTO;

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
    private Blob img;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private java.util.List<GrupoalbumartistaEntity> grupoalbumartistas;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private java.util.List<ResenyaEntity> resenyas;

    public AlbumEntity() {
        this.grupoalbumartistas = new java.util.ArrayList<>();
        this.resenyas = new java.util.ArrayList<>();
    }

    public String getImgBase64() {
        if (img != null) {
            try (InputStream is = img.getBinaryStream()) {
                byte[] bytes = is.readAllBytes();
                return Base64.getEncoder().encodeToString(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null; // Or a default placeholder image
    }

    public static AlbumDTO convertToDTO(AlbumEntity albumEntity) {
        AlbumDTO dto = new AlbumDTO();
        dto.setId(albumEntity.getId());
        dto.setNombre(albumEntity.getNombre());
        dto.setFecha(albumEntity.getFecha());
        dto.setGenero(albumEntity.getGenero());
        dto.setDescripcion(albumEntity.getDescripcion());
        dto.setDiscografica(albumEntity.getDiscografica());
        dto.setGrupoalbumartistas(albumEntity.getGrupoalbumartistas());
        dto.setResenyas(albumEntity.getResenyas());
        if (albumEntity.getImg() != null) {
            try (InputStream is = albumEntity.getImg().getBinaryStream()) {
                byte[] bytes = is.readAllBytes();
                dto.setImgBase64(Base64.getEncoder().encodeToString(bytes));
            } catch (Exception e) {
                e.printStackTrace();
                dto.setImgBase64(null);
            }
        }

        return dto;
    }

    public static AlbumEntity convertToEntity(AlbumDTO albumDTO) {
        AlbumEntity entity = new AlbumEntity();
        entity.setId(albumDTO.getId());
        entity.setNombre(albumDTO.getNombre());
        entity.setFecha(albumDTO.getFecha());
        entity.setGenero(albumDTO.getGenero());
        entity.setDescripcion(albumDTO.getDescripcion());
        entity.setDiscografica(albumDTO.getDiscografica());

        if (albumDTO.getImgBase64() != null) {
            try {
                byte[] bytes = Base64.getDecoder().decode(albumDTO.getImgBase64());
                entity.setImg(new javax.sql.rowset.serial.SerialBlob(bytes));
            } catch (Exception e) {
                e.printStackTrace();
                entity.setImg(null);
            }
        }

        return entity;
    }

    public int getGrupoalbumartistas() {
        return grupoalbumartistas.size();
    }

    public int getResenyas() {
        return resenyas.size();
    }

}
