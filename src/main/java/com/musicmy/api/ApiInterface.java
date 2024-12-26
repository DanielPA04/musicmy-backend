package com.musicmy.api;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ApiInterface<T> {
    @GetMapping("")
    public ResponseEntity<Page<T>> getPage(Pageable oPageable, @RequestParam Optional<String> filter);

    @GetMapping("/count")
    public ResponseEntity<Long> count();

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id);

    @PutMapping("")
    public ResponseEntity<T> create(@RequestBody T object);

    @PostMapping("")
    public ResponseEntity<T> update(@RequestBody T object);

    @PutMapping("/random/{cantidad}")
    public ResponseEntity<Long> randomCreate(@PathVariable Long cantidad);

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll();

}
