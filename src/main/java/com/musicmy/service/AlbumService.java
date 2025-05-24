package com.musicmy.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musicmy.entity.AlbumEntity;
import com.musicmy.exception.ResourceNotFoundException;
import com.musicmy.repository.AlbumRepository;
import com.musicmy.specifications.AlbumSpecifications;

import jakarta.transaction.Transactional;

@Service
public class AlbumService implements ServiceInterface<AlbumEntity> {

    @Autowired
    private AlbumRepository oAlbumRepository;

    @Autowired
    private RandomService oRandomService;

    @Override
    public Long baseCreate() {
        try {
            // Leer el JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ClassPathResource resource = new ClassPathResource("json/album.json");
            List<AlbumEntity> albumes = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
            });

            reiniciarAutoIncrement();

            for (AlbumEntity album : albumes) {

                // Cargar imagen desde resources/img si existe
                byte[] imagen = cargarImagenDesdeResources("img/" + album.getNombre().replace(" ", "").toLowerCase() + ".webp");
                if (imagen == null) {
                    imagen = cargarImagenDesdeResources("img/album.webp");
                }

                album.setImg(imagen);

                // Guardar en la base de datos
                oAlbumRepository.save(album);
            }
            return oAlbumRepository.count();
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
        oAlbumRepository.flush(); // Asegurar que los cambios han sido guardados
        oAlbumRepository.resetAutoIncrement();

    }

    @Override
    public AlbumEntity randomSelection() {
        return oAlbumRepository.findAll().get(oRandomService.getRandomInt(0, (int) oAlbumRepository.count() - 1));
    }

    @Override
    public Page<AlbumEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oAlbumRepository
                    .findByNombreContainingOrGeneroContainingOrDescripcionContainingOrDiscograficaContaining(
                            filter.get(), filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oAlbumRepository.findAll(oPageable);
        }
    }

    @Override
    public AlbumEntity get(Long id) {
        return oAlbumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album con el id " + id + " no encontrado"));
    }

    public List<AlbumEntity> getByIdArtista(Long id) {
        return oAlbumRepository.findByArtistaId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album con el id " + id + " no encontrado"));
    }

    public Double getNotaMedia(Long id) {
        return oAlbumRepository.getNotaMedia(id).orElse(0.0);
    }

    public Page<AlbumEntity> getPageLastMonth(Pageable pageable) {
        return oAlbumRepository.findByFechaBetween(LocalDate.now().minusMonths(1), LocalDate.now(), pageable);
    }

    public Page<AlbumEntity> getPageNew(Pageable pageable) {
        return oAlbumRepository.findAllByOrderByFechaDesc(pageable);
    }

     // albumes mas populares (mas reseñas en total)
    public Page<AlbumEntity> getMostPopularAlbums(Pageable pageable) {
        return oAlbumRepository.findAllByOrderByResenyaCountDesc(pageable);
    }

 public Page<AlbumEntity> getTopRated(
      String genero,
      String discografica,
      String nombre,
      Pageable pageable) {
    return oAlbumRepository.findFilteredTopRated(
      genero == null ? null : genero.toLowerCase().trim(),
      discografica == null ? null : discografica.toLowerCase().trim(),
      nombre == null ? null : nombre.toLowerCase().trim(),
      pageable
    );
  }

  public Page<AlbumEntity> getPopularRecent(
      String genero,
      String discografica,
      String nombre,
      Pageable pageable) {
    LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
    return oAlbumRepository.findFilteredPopularSince(
      threeMonthsAgo,
      genero == null ? null : genero.toLowerCase().trim(),
      discografica == null ? null : discografica.toLowerCase().trim(),
      nombre == null ? null : nombre.toLowerCase().trim(),
      pageable
    );
  }

     public Page<AlbumEntity> search(
            String genero,
            String discografica,
            String nombre,
            Pageable pageable) {
        Specification<AlbumEntity> spec = AlbumSpecifications.filter(
            genero, discografica, nombre
        );
        return oAlbumRepository.findAll(spec, pageable);
    }

    public byte[] getImgById(Long id) {
        AlbumEntity album = get(id);
        return album.getImg();
    }

    
        
    


    @Override
    public Long count() {
        return oAlbumRepository.count();
    }

    @Override
    public Long delete(Long id) {
        oAlbumRepository.deleteById(id);
        return 1L;
    }

    @Override
    public AlbumEntity create(AlbumEntity oAlbumEntity) {
        return oAlbumRepository.save(oAlbumEntity);
    }

    @Override
    public AlbumEntity update(AlbumEntity oAlbumEntity) {
        AlbumEntity oAlbumEntityFromDatabase = oAlbumRepository.findById(oAlbumEntity.getId()).get();
        if (oAlbumEntity.getNombre() != null) {
            oAlbumEntityFromDatabase.setNombre(oAlbumEntity.getNombre());
        }
        if (oAlbumEntity.getFecha() != null) {
            oAlbumEntityFromDatabase.setFecha(oAlbumEntity.getFecha());
        }
        if (oAlbumEntity.getGenero() != null) {
            oAlbumEntityFromDatabase.setGenero(oAlbumEntity.getGenero());
        }
        if (oAlbumEntity.getDescripcion() != null) {
            oAlbumEntityFromDatabase.setDescripcion(oAlbumEntity.getDescripcion());
        }
        if (oAlbumEntity.getDiscografica() != null) {
            oAlbumEntityFromDatabase.setDiscografica(oAlbumEntity.getDiscografica());
        }
        if (oAlbumEntity.getImg() != null) {
            oAlbumEntityFromDatabase.setImg(oAlbumEntity.getImg());
        }
        return oAlbumRepository.save(oAlbumEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oAlbumRepository.deleteAll();
        return this.count();
    }

}
