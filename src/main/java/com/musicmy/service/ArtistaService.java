package com.musicmy.service;

import java.util.List;
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

    @Autowired
    private AuthService oAuthService;

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
            oArtistaEntity.setNombrereal(nombresReales[oRandomService.getRandomInt(0, nombresReales.length - 1)]);
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
                    .findByNombreContainingOrNombrerealContainingOrDescripcionContaining(
                            filter.get(), filter.get(), filter.get(), oPageable);
        } else {
            return oArtistaRepository.findAll(oPageable);
        }
    }

    @Override
    public ArtistaEntity get(Long id) {
        return oArtistaRepository.findById(id).get();
    }

    public List<ArtistaEntity> getByIdAlbum(Long id) {
        return oArtistaRepository.findByAlbumId(id).get();
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
            // TODO
            throw new UnsupportedOperationException("Not supported");
        }
    }

    @Override
    public ArtistaEntity create(ArtistaEntity oArtistaEntity) {
        if (oAuthService.isAdministrador()) {
            return oArtistaRepository.save(oArtistaEntity);
        } else {
            // TODO
            throw new UnsupportedOperationException("Not supported");
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
