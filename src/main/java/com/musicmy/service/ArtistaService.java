package com.musicmy.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicmy.entity.ArtistaEntity;
import com.musicmy.repository.ArtistaRepository;

@Service
public class ArtistaService implements ServiceInterface<ArtistaEntity> {

    @Autowired
    private ArtistaRepository oArtistaRepository;

    @Autowired
    private RandomService oRandomService;


    String[] nombres = {
        "Bad Bunny",
        "Taylor Swift",
        "Drake",
        "Adele",
        "Billie Eilish"
    };
    
    String[] nombresReales = {
        "Benito Antonio Martínez Ocasio",
        "Taylor Alison Swift",
        "Aubrey Drake Graham",
        "Adele Laurie Blue Adkins",
        "Billie Eilish Pirate Baird O'Connell"
    };
    
    String[] descripciones = {
        "Cantante y rapero puertorriqueño, pionero del trap latino.",
        "Cantante y compositora estadounidense conocida por su versatilidad.",
        "Rapper y productor canadiense, un ícono del hip-hop contemporáneo.",
        "Cantante británica famosa por su poderosa voz y baladas emotivas.",
        "Cantante y compositora estadounidense, conocida por su estilo alternativo y minimalista."
    };
    
    String[] spotify = {
        "https://open.spotify.com/artist/4q3ewBCX7sLwd24euuV69X",
        "https://open.spotify.com/artist/06HL4z0CvFAxyc27GXpf02",
        "https://open.spotify.com/artist/3TVXtAsR1Inumwj472S9r4",
        "https://open.spotify.com/artist/4dpARuHxo51G3z768sgnrY",
        "https://open.spotify.com/artist/6qqNVTkY8uBg9cP3Jd7DAH"
    };

    @Override
    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            ArtistaEntity oArtistaEntity = new ArtistaEntity();
            oArtistaEntity.setNombre(nombres[oRandomService.getRandomInt(0, nombres.length - 1)]);
            oArtistaEntity.setNombreReal(nombresReales[oRandomService.getRandomInt(0, nombresReales.length - 1)]);
            oArtistaEntity.setDescripcion(descripciones[oRandomService.getRandomInt(0, descripciones.length - 1)]);
            oArtistaEntity.setSpotify(spotify[oRandomService.getRandomInt(0, spotify.length - 1)]);
            oArtistaEntity.setImg(null);
            oArtistaRepository.save(oArtistaEntity);
        }
        return oArtistaRepository.count();
    }

    @Override
    public ArtistaEntity randomSelection() {
        return oArtistaRepository.findAll().get(oRandomService.getRandomInt(0, (int) oArtistaRepository.count() - 1));
    }

    @Override
    public Page<ArtistaEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oArtistaRepository
                    .findByNombreContainingOrNombreRealContainingOrDescripcionContaining(
                            filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oArtistaRepository.findAll(oPageable);
        }
    }

    @Override
    public ArtistaEntity get(Long id) {
        return oArtistaRepository.findById(id).get();
    }

    @Override
    public Long count() {
        return oArtistaRepository.count();
    }

    @Override
    public Long delete(Long id) {
        oArtistaRepository.deleteById(id);
        return 1L;
    }

    @Override
    public ArtistaEntity create(ArtistaEntity oUsuarioEntity) {
        return oArtistaRepository.save(oUsuarioEntity);
    }

    @Override
    public ArtistaEntity update(ArtistaEntity oUsuarioEntity) {
        ArtistaEntity oArtistaEntityFromDatabase = oArtistaRepository.findById(oUsuarioEntity.getId()).get();
        if (oUsuarioEntity.getNombre() != null) {
            oArtistaEntityFromDatabase.setNombre(oUsuarioEntity.getNombre());
        }
        if (oUsuarioEntity.getNombreReal() != null) {
            oArtistaEntityFromDatabase.setNombreReal(oUsuarioEntity.getNombreReal());
        }
        if (oUsuarioEntity.getDescripcion() != null) {
            oArtistaEntityFromDatabase.setDescripcion(oUsuarioEntity.getDescripcion());
        }
        if (oUsuarioEntity.getSpotify() != null) {
            oArtistaEntityFromDatabase.setSpotify(oUsuarioEntity.getSpotify());
        }
        if (oUsuarioEntity.getImg() != null) {
            oArtistaEntityFromDatabase.setImg(oUsuarioEntity.getImg());
        }
        return oArtistaRepository.save(oArtistaEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oArtistaRepository.deleteAll();
        return this.count();
    }

}
