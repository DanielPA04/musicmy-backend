package com.musicmy.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.musicmy.entity.ArtistaEntity;
import com.musicmy.service.ArtistaService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/artista")
public class Artista {
    @Autowired
    ArtistaService oArtistaService;

    @GetMapping("")
    public ResponseEntity<Page<ArtistaEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<ArtistaEntity>>(oArtistaService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oArtistaService.count(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaEntity> get(@PathVariable Long id) {
        return new ResponseEntity<ArtistaEntity>(oArtistaService.get(id), HttpStatus.OK);
    }

    @GetMapping("/byalbum/{id}")
    public ResponseEntity<List<ArtistaEntity>> getByAlbum(@PathVariable Long id) {
        return new ResponseEntity<List<ArtistaEntity>>(oArtistaService.getByIdAlbum(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oArtistaService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ArtistaEntity> create(@RequestBody ArtistaEntity ArtistaEntity) {
        return new ResponseEntity<ArtistaEntity>(oArtistaService.create(ArtistaEntity), HttpStatus.OK);
    }

    @PostMapping("/img")
    public ResponseEntity<ArtistaEntity> create(
            @RequestParam("nombre") String nombre,
            @RequestParam("nombrereal") String nombrereal,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("spotify") String spotify,
            @RequestParam("img") MultipartFile img) {

        try {

            return new ResponseEntity<>(oArtistaService.create(new ArtistaEntity(nombre, nombrereal, descripcion, spotify, img.getBytes())), HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<ArtistaEntity> update(@RequestBody ArtistaEntity ArtistaEntity) {
        return new ResponseEntity<ArtistaEntity>(oArtistaService.update(ArtistaEntity), HttpStatus.OK);
    }

    @PutMapping("/img")
    public ResponseEntity<ArtistaEntity> update(
            @RequestParam("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("nombrereal") String nombrereal,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("spotify") String spotify,
            @RequestParam("img") MultipartFile img) {

        try {

            ArtistaEntity artistaEntity = new ArtistaEntity();
            artistaEntity.setId(id);
            artistaEntity.setNombre(nombre);
            artistaEntity.setNombrereal(nombrereal);
            artistaEntity.setDescripcion(descripcion);
            artistaEntity.setSpotify(spotify);
            artistaEntity.setImg(img.getBytes());
            return new ResponseEntity<>(oArtistaService.update(artistaEntity), HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/fill")
    public ResponseEntity<Long> fill() {
        return new ResponseEntity<Long>(oArtistaService.baseCreate(), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<Long>(oArtistaService.deleteAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}/img")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(oArtistaService.getImgById(id));
    }
}
