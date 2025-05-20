package com.musicmy.api;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicmy.dto.UsuarioRankingDTO;
import com.musicmy.entity.TipousuarioEntity;
import com.musicmy.entity.UsuarioEntity;
import com.musicmy.service.UsuarioService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/usuario")
public class Usuario {
    @Autowired
    UsuarioService oUsuarioService;

    @GetMapping("")
    public ResponseEntity<Page<UsuarioEntity>> getPage(
            Pageable oPageable,
            @RequestParam Optional<String> filter) {
        return new ResponseEntity<Page<UsuarioEntity>>(oUsuarioService.getPage(oPageable, filter), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oUsuarioService.count(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> get(@PathVariable Long id) {
        return new ResponseEntity<UsuarioEntity>(oUsuarioService.get(id), HttpStatus.OK);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<UsuarioRankingDTO>> getTop20Usuarios() {
        return ResponseEntity.ok(oUsuarioService.getTop20Usuarios());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<Long>(oUsuarioService.delete(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<UsuarioEntity> create(@RequestBody UsuarioEntity UsuarioEntity) {
        return new ResponseEntity<UsuarioEntity>(oUsuarioService.create(UsuarioEntity), HttpStatus.OK);
    }

    @PostMapping("/create/img")
    public ResponseEntity<UsuarioEntity> create(
            @RequestParam("username") String username,
            @RequestParam("nombre") String nombre,
            @RequestParam("fecha") String fecha,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("website") String website,
            @RequestParam("img") MultipartFile img,
            @RequestParam("tipousuario") String tipousuarioJson) {

        try {

            return new ResponseEntity<>(
                    oUsuarioService.create(
                            new UsuarioEntity(username, nombre, LocalDate.parse(fecha), descripcion,
                                    email, password, website, img.getBytes(),
                                    new ObjectMapper().readValue(tipousuarioJson, TipousuarioEntity.class))),
                    HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioEntity> register(@RequestBody UsuarioEntity UsuarioEntity) {
        return new ResponseEntity<UsuarioEntity>(oUsuarioService.register(UsuarioEntity), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<UsuarioEntity> update(@RequestBody UsuarioEntity UsuarioEntity) {
        return new ResponseEntity<UsuarioEntity>(oUsuarioService.update(UsuarioEntity), HttpStatus.OK);
    }

    @PutMapping("/update/img")
    public ResponseEntity<UsuarioEntity> update(
            @RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("nombre") String nombre,
            @RequestParam("fecha") String fecha,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("website") String website,
            @RequestParam("img") MultipartFile img,
            @RequestParam("tipousuario") String tipousuarioJson) {

        try {

            return new ResponseEntity<>(
                    oUsuarioService.update(
                            new UsuarioEntity(id, username, nombre, LocalDate.parse(fecha), descripcion,
                                    email, password, website, img.getBytes(),
                                    new ObjectMapper().readValue(tipousuarioJson, TipousuarioEntity.class))),
                    HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/fill")
    public ResponseEntity<Long> create() {
        return new ResponseEntity<Long>(oUsuarioService.baseCreate(), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Long> deleteAll() {
        return new ResponseEntity<Long>(oUsuarioService.deleteAll(), HttpStatus.OK);
    }

    @GetMapping("/byemail/{email}")
    public ResponseEntity<UsuarioEntity> getByEmail(@PathVariable String email) {
        return new ResponseEntity<UsuarioEntity>(oUsuarioService.getByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        return new ResponseEntity<Boolean>(oUsuarioService.checkIfEmailExists(email), HttpStatus.OK);
    }

    @GetMapping("/check/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        return new ResponseEntity<Boolean>(oUsuarioService.checkIfUsernameExists(username), HttpStatus.OK);
    }

    @GetMapping("/{id}/img")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(oUsuarioService.getImgById(id));
    }
}
