package com.musicmy.api;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.musicmy.dto.ResenyaWithLikeCountDTO;
import com.musicmy.entity.ResenyaEntity;
import com.musicmy.service.ResenyaService;
import org.springframework.data.domain.Sort;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/resenya")
public class Resenya {
    @Autowired
    ResenyaService oResenyaService;

    @GetMapping("")
    public ResponseEntity<Page<ResenyaEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<ResenyaEntity>>(oResenyaService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/byusuario/{id}")
    public ResponseEntity<Page<ResenyaEntity>> getPageByUsuario(
            Pageable oPageable,
            @PathVariable Long id) {
        return new ResponseEntity<Page<ResenyaEntity>>(oResenyaService.getPageByUsuario(id, oPageable), HttpStatus.OK);
    }

    @GetMapping("/byusuario/{id}/recent")
    public ResponseEntity<Page<ResenyaEntity>> getResenyasRecientes(
            @PathVariable("id") Long id,
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(oResenyaService.getResenyasRecientesByUsuario(id, pageable));
    }

    @GetMapping("/byusuario/{id}/best")
    public ResponseEntity<Page<ResenyaEntity>> getResenyasTop(
            @PathVariable("id") Long id,
            @PageableDefault(size = 10, sort = "nota", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(oResenyaService.getResenyasTopByUsuario(id, pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oResenyaService.count(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenyaEntity> get(@PathVariable Long id) {
        return new ResponseEntity<ResenyaEntity>(oResenyaService.get(id), HttpStatus.OK);
    }

    @GetMapping("/byalbum/{id}")
    public ResponseEntity<Page<ResenyaWithLikeCountDTO>> getPageByAlbum(
            Pageable oPageable,
            @PathVariable Long id) {
        Page<ResenyaEntity> page = oResenyaService.getPageByAlbum(id, oPageable);
        Page<ResenyaWithLikeCountDTO> dtoPage = page.map(resenya -> oResenyaService.toDtoWithLikeCount(resenya));
        return ResponseEntity.ok(dtoPage);
    }

       @GetMapping("/byalbum/likes/{id}")
    public ResponseEntity<Page<ResenyaWithLikeCountDTO>> getPageByAlbumAndLikes(
            Pageable oPageable,
            @PathVariable Long id) {
        Page<ResenyaEntity> page = oResenyaService.getPageByAlbumAndLikes(id, oPageable);
        Page<ResenyaWithLikeCountDTO> dtoPage = page.map(resenya -> oResenyaService.toDtoWithLikeCount(resenya));
        return ResponseEntity.ok(dtoPage);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oResenyaService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResenyaEntity> create(@RequestBody ResenyaEntity ResenyaEntity) {
        return new ResponseEntity<ResenyaEntity>(oResenyaService.create(ResenyaEntity), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<ResenyaEntity> update(@RequestBody ResenyaEntity oResenyaEntity) {
        return new ResponseEntity<ResenyaEntity>(oResenyaService.update(oResenyaEntity), HttpStatus.OK);
    }

    @PostMapping("/fill")
    public ResponseEntity<Long> fill() {
        return new ResponseEntity<Long>(oResenyaService.baseCreate(), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<Long>(oResenyaService.deleteAll(), HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> exists(@RequestBody ResenyaEntity oResenyaEntity) {
        return new ResponseEntity<Boolean>(oResenyaService.isResenyaAlreadyExists(oResenyaEntity), HttpStatus.OK);
    }

    @GetMapping("/check/{email}/{albumId}")
    public ResponseEntity<Boolean> existsByEmailAndAlbumId(@PathVariable String email, @PathVariable Long albumId) {
        return new ResponseEntity<Boolean>(oResenyaService.isResenyaAlreadyExists(email, albumId), HttpStatus.OK);
    }

    @GetMapping("/find/{email}/{albumId}")
    public ResponseEntity<ResenyaEntity> getResenyaByEmailAndAlbumId(@PathVariable String email,
            @PathVariable Long albumId) {
        return new ResponseEntity<ResenyaEntity>(
                oResenyaService.getByAlbumAndUsuario(email, albumId), HttpStatus.OK);
    }
}
