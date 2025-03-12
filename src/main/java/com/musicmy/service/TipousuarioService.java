package com.musicmy.service;

import java.io.IOException;
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
import com.musicmy.entity.TipousuarioEntity;
import com.musicmy.exception.UnauthorizedAccessException;
import com.musicmy.repository.TipousuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class TipousuarioService implements ServiceInterface<TipousuarioEntity> {

    @Autowired
    private TipousuarioRepository oTipousuarioRepository;

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
            ClassPathResource resource = new ClassPathResource("json/tipousuario.json");
            List<TipousuarioEntity> tiposusuario = objectMapper.readValue(resource.getInputStream(),
                    new TypeReference<>() {
                    });

            reiniciarAutoIncrement();

            for (TipousuarioEntity tipousuario : tiposusuario) {
                oTipousuarioRepository.save(tipousuario);
            }
            return oTipousuarioRepository.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Transactional
    public void reiniciarAutoIncrement() {
        oTipousuarioRepository.flush(); // Asegurar que los cambios han sido guardados
        oTipousuarioRepository.resetAutoIncrement();

    }

    @Override
    public TipousuarioEntity create(TipousuarioEntity oTipousuarioEntity) {
        if (oAuthService.isAdministrador()) {
            return oTipousuarioRepository.save(oTipousuarioEntity);
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public TipousuarioEntity update(TipousuarioEntity oTipousuarioEntity) {
        if (oAuthService.isAdministrador()) {
            return oTipousuarioRepository.save(oTipousuarioEntity);
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public Long delete(Long id) {
        if (oAuthService.isAdministrador()) {
            oTipousuarioRepository.deleteById(id);
            return 1L;
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

    @Override
    public TipousuarioEntity randomSelection() {
        return oTipousuarioRepository.findAll()
                .get(oRandomService.getRandomInt(0, (int) oTipousuarioRepository.count() - 1));
    }

    @Override
    public Page<TipousuarioEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oTipousuarioRepository
                    .findByNombreContaining(
                            filter.get(), oPageable);
        } else {
            return oTipousuarioRepository.findAll(oPageable);
        }
    }

    @Override
    public TipousuarioEntity get(Long id) {
        return oTipousuarioRepository.findById(id).get();
    }

    @Override
    public Long count() {
        return oTipousuarioRepository.count();
    }

    @Override
    public Long deleteAll() {
        // if (oAuthService.isAdministrador()) {
            oTipousuarioRepository.deleteAll();
            return this.count();
        // } else {
        //     throw new UnauthorizedAccessException("No autorizado");
        // }
    }

}
