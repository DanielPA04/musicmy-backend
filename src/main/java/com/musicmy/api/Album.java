package com.musicmy.api;

import org.springframework.web.bind.annotation.RestController;
import com.musicmy.entity.AlbumEntity;
import com.musicmy.service.AlbumService;
import java.util.Optional;
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
    public ResponseEntity<Page<AlbumEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<AlbumEntity>>(oAlbumService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oAlbumService.count(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumEntity> get(@PathVariable Long id) {
        return new ResponseEntity<AlbumEntity>(oAlbumService.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oAlbumService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<AlbumEntity> create(@RequestBody AlbumEntity AlbumEntity) {
        return new ResponseEntity<AlbumEntity>(oAlbumService.create(AlbumEntity), HttpStatus.OK);
    }

    

    @PutMapping("")
    public ResponseEntity<AlbumEntity> update(@RequestBody AlbumEntity AlbumEntity) {
        return new ResponseEntity<AlbumEntity>(oAlbumService.update(AlbumEntity), HttpStatus.OK);
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
