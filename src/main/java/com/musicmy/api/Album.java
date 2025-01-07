package com.musicmy.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.musicmy.dto.AlbumDTO;
import com.musicmy.entity.AlbumEntity;
import com.musicmy.service.AlbumService;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;




@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/album")
public class Album {

    @Autowired
    AlbumService oAlbumService;

    @GetMapping("")
    public ResponseEntity<Page<AlbumDTO>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
    
        Page<AlbumEntity> albumEntities = oAlbumService.getPage(oPageable, filter);
    
        Page<AlbumDTO> albumDTOs = albumEntities.map(AlbumEntity::convertToDTO);
    
        return new ResponseEntity<>(albumDTOs, HttpStatus.OK);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oAlbumService.count(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> get(@PathVariable Long id) {
        AlbumEntity albumEntity = oAlbumService.get(id);
        if (albumEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AlbumDTO albumDTO = AlbumEntity.convertToDTO(albumEntity);
        return new ResponseEntity<>(albumDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oAlbumService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<AlbumEntity> create(@RequestBody AlbumEntity AlbumEntity) {
        return new ResponseEntity<AlbumEntity>(oAlbumService.create(AlbumEntity), HttpStatus.OK);
    }

    @PostMapping("/img")
public ResponseEntity<AlbumEntity> create(
        @RequestParam("nombre") String nombre,
        @RequestParam("fecha") String fecha,
        @RequestParam("genero") String genero,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("discografica") String discografica,
        @RequestParam("img") MultipartFile img) {

    try {

        Blob imageBlob = new SerialBlob(img.getBytes()); 

        AlbumEntity albumEntity = new AlbumEntity();
        albumEntity.setNombre(nombre);
        albumEntity.setFecha(LocalDate.parse(fecha));
        albumEntity.setGenero(genero);
        albumEntity.setDescripcion(descripcion);
        albumEntity.setDiscografica(discografica);
        albumEntity.setImg(imageBlob); 

        AlbumEntity savedAlbum = oAlbumService.create(albumEntity);
        return new ResponseEntity<>(savedAlbum, HttpStatus.OK);

    } catch (IOException | SQLException e) {
        e.printStackTrace();
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @PutMapping("")
    public ResponseEntity<AlbumEntity> update(@RequestBody AlbumEntity AlbumEntity) {
        return new ResponseEntity<AlbumEntity>(oAlbumService.update(AlbumEntity), HttpStatus.OK);
    }

    @PutMapping("/img")
    public ResponseEntity<AlbumEntity> updateDTO(@RequestBody AlbumDTO oAlbumDTO) {
        AlbumEntity oAlbumEntity = AlbumEntity.convertToEntity(oAlbumDTO);
        return new ResponseEntity<AlbumEntity>(oAlbumService.update(oAlbumEntity), HttpStatus.OK);
    }

    @PostMapping("/random/{cantidad}")
    public ResponseEntity<Long> create(@PathVariable Long cantidad) {
        return new ResponseEntity<Long>(oAlbumService.randomCreate(cantidad), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<Long>(oAlbumService.deleteAll(), HttpStatus.OK);
    }
    
}
