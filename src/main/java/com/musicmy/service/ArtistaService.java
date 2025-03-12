package com.musicmy.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musicmy.entity.ArtistaEntity;
import com.musicmy.exception.ResourceNotFoundException;
import com.musicmy.exception.UnauthorizedAccessException;
import com.musicmy.repository.ArtistaRepository;

import jakarta.transaction.Transactional;

@Service
public class ArtistaService implements ServiceInterface<ArtistaEntity> {

    @Autowired
    private ArtistaRepository oArtistaRepository;

    @Autowired
    private RandomService oRandomService;

    @Autowired
    private AuthService oAuthService;

   

   @Override
    public Long baseCreate() {
        try {
            // Leer el JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ClassPathResource resource = new ClassPathResource("json/artista.json");
            List<ArtistaEntity> artistas = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
            });

            reiniciarAutoIncrement();

            for (ArtistaEntity artista : artistas) {

                // Cargar imagen desde resources/img si existe
                byte[] imagen = cargarImagenDesdeResources("img/" + artista.getNombre().replace(" ", "").toLowerCase() + ".webp");
                if (imagen == null) {
                    imagen = cargarImagenDesdeResources("img/artista.webp");
                }

                artista.setImg(imagen);

                // Guardar en la base de datos
                oArtistaRepository.save(artista);
            }
            return oArtistaRepository.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private byte[] cargarImagenDesdeResources(String ruta) {
        try {
            ClassPathResource resource = new ClassPathResource(ruta);
            Path path = resource.getFile().toPath();
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen: " + ruta);
            return null;
        }
    }

    @Transactional
    public void reiniciarAutoIncrement() {
        oArtistaRepository.flush(); // Asegurar que los cambios han sido guardados
        oArtistaRepository.resetAutoIncrement();

    }
    @Override
    public ArtistaEntity randomSelection() {
        return oArtistaRepository.findAll().get(oRandomService.getRandomInt(0, (int) oArtistaRepository.count() - 1));
    }

    @Override
    public Page<ArtistaEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oArtistaRepository
                    .findByNombreContainingOrNombrerealContainingOrDescripcionContaining(
                            filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oArtistaRepository.findAll(oPageable);
        }
    }

    @Override
    public ArtistaEntity get(Long id) {
        return oArtistaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artista con el id " + id + " no encontrado"));
    }

    public List<ArtistaEntity> getByIdAlbum(Long id) {
        return oArtistaRepository.findByAlbumId(id).get();
    }

      public byte[] getImgById(Long id) {
        ArtistaEntity artista = get(id);
        return artista.getImg();
    }

    @Override
    public Long count() {
        return oArtistaRepository.count();
    }

    @Override
    public Long delete(Long id) {
        if (oAuthService.isAdministrador()) {
            oArtistaRepository.deleteById(id);
            return 1L;
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }

    }

    @Override
    public ArtistaEntity create(ArtistaEntity oArtistaEntity) {
        if (oAuthService.isAdministrador()) {
            return oArtistaRepository.save(oArtistaEntity);
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public ArtistaEntity update(ArtistaEntity oArtistaEntity) {
        ArtistaEntity oArtistaEntityFromDatabase = oArtistaRepository.findById(oArtistaEntity.getId()).get();
        if (oArtistaEntity.getNombre() != null) {
            oArtistaEntityFromDatabase.setNombre(oArtistaEntity.getNombre());
        }
        if (oArtistaEntity.getNombrereal() != null) {
            oArtistaEntityFromDatabase.setNombrereal(oArtistaEntity.getNombrereal());
        }
        if (oArtistaEntity.getDescripcion() != null) {
            oArtistaEntityFromDatabase.setDescripcion(oArtistaEntity.getDescripcion());
        }
        if (oArtistaEntity.getSpotify() != null) {
            oArtistaEntityFromDatabase.setSpotify(oArtistaEntity.getSpotify());
        }
        if (oArtistaEntity.getImg() != null) {
            oArtistaEntityFromDatabase.setImg(oArtistaEntity.getImg());
        }
        return oArtistaRepository.save(oArtistaEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oArtistaRepository.deleteAll();
        return this.count();
    }

}
