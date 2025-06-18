package com.musicmy.api;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.musicmy.entity.AlbumEntity;
import com.musicmy.entity.ArtistaEntity;
import com.musicmy.entity.GrupoalbumartistaEntity;
import com.musicmy.service.GrupoalbumartistaService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/initial/grupoalbumartista")
public class Grupoalbumartista {
    @Autowired
    GrupoalbumartistaService oGrupoalbumartistaService;

    @GetMapping("")
    public ResponseEntity<Page<GrupoalbumartistaEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<GrupoalbumartistaEntity>>(oGrupoalbumartistaService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oGrupoalbumartistaService.count(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoalbumartistaEntity> get(@PathVariable Long id) {
        return new ResponseEntity<GrupoalbumartistaEntity>(oGrupoalbumartistaService.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oGrupoalbumartistaService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<GrupoalbumartistaEntity> create(@RequestBody GrupoalbumartistaEntity GrupoalbumartistaEntity) {
        return new ResponseEntity<GrupoalbumartistaEntity>(oGrupoalbumartistaService.create(GrupoalbumartistaEntity), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<GrupoalbumartistaEntity> update(@RequestBody GrupoalbumartistaEntity GrupoalbumartistaEntity) {
        return new ResponseEntity<GrupoalbumartistaEntity>(oGrupoalbumartistaService.update(GrupoalbumartistaEntity), HttpStatus.OK);
    }

    @PostMapping("/fill")
    public ResponseEntity<Long> fill() {
        return new ResponseEntity<Long>(oGrupoalbumartistaService.baseCreate(), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<Long>(oGrupoalbumartistaService.deleteAll(), HttpStatus.OK);
    }


    @PutMapping("/artistas/{id}")
    public List<ArtistaEntity> updateArtistasToAlbum(@RequestBody List<ArtistaEntity> artistas, @PathVariable Long id) {
        return oGrupoalbumartistaService.updateArtistasToAlbum(artistas, id);
    }

    @PutMapping("/albumes/{id}")
    public List<ArtistaEntity> updateAlbumesToArtista(@RequestBody List<AlbumEntity> albumes, @PathVariable Long id) {
        return oGrupoalbumartistaService.updateAlbumesToArtista(albumes, id);
    }
}