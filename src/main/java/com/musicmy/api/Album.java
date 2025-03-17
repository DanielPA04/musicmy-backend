package com.musicmy.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.musicmy.entity.AlbumEntity;
import com.musicmy.service.AlbumService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Page<AlbumEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<>(oAlbumService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/lastmonth")
    public ResponseEntity<Page<AlbumEntity>> getPageLastMonth(Pageable oPageable) {
        return new ResponseEntity<>(oAlbumService.getPageLastMonth(oPageable), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oAlbumService.count(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumEntity> get(@PathVariable Long id) {
        return new ResponseEntity<>(oAlbumService.get(id), HttpStatus.OK);
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Double> getMedia(@PathVariable Long id) {
        return new ResponseEntity<Double>(oAlbumService.getNotaMedia(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oAlbumService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<AlbumEntity> create(@RequestBody AlbumEntity oAlbumEntity) {
        return new ResponseEntity<AlbumEntity>(oAlbumService.create(oAlbumEntity), HttpStatus.OK);
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

            AlbumEntity albumEntity = new AlbumEntity();
            albumEntity.setNombre(nombre);
            albumEntity.setFecha(LocalDate.parse(fecha));
            albumEntity.setGenero(genero);
            albumEntity.setDescripcion(descripcion);
            albumEntity.setDiscografica(discografica);
            albumEntity.setImg(img.getBytes());
            return new ResponseEntity<>(oAlbumService.create(albumEntity), HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<AlbumEntity> update(@RequestBody AlbumEntity AlbumEntity) {
        return new ResponseEntity<AlbumEntity>(oAlbumService.update(AlbumEntity), HttpStatus.OK);
    }

    @PutMapping("/img")
    public ResponseEntity<AlbumEntity> update(
            @RequestParam("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("fecha") String fecha,
            @RequestParam("genero") String genero,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("discografica") String discografica,
            @RequestParam("img") MultipartFile img) {

        try {

            AlbumEntity albumEntity = new AlbumEntity();
            albumEntity.setId(id);
            albumEntity.setNombre(nombre);
            albumEntity.setFecha(LocalDate.parse(fecha));
            albumEntity.setGenero(genero);
            albumEntity.setDescripcion(descripcion);
            albumEntity.setDiscografica(discografica);
            albumEntity.setImg(img.getBytes());
            AlbumEntity savedAlbum = oAlbumService.update(albumEntity);
            return new ResponseEntity<>(savedAlbum, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/fill")
    public ResponseEntity<Long> fill() {
        return new ResponseEntity<Long>(oAlbumService.baseCreate(), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<Long>(oAlbumService.deleteAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}/img")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(oAlbumService.getImgById(id));
    }

    @GetMapping("/byartista/{id}")
    public ResponseEntity<List<AlbumEntity>> getByAlbum(@PathVariable Long id) {
        return new ResponseEntity<List<AlbumEntity>>(oAlbumService.getByIdArtista(id), HttpStatus.OK);
    }
}
