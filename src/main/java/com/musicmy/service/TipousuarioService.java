package com.musicmy.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicmy.entity.TipousuarioEntity;
import com.musicmy.exception.UnauthorizedAccessException;
import com.musicmy.repository.TipousuarioRepository;

@Service
public class TipousuarioService implements ServiceInterface<TipousuarioEntity> {

    @Autowired
    private TipousuarioRepository oTipousuarioRepository;

    @Autowired
    private RandomService oRandomService;

    @Autowired
    private AuthService oAuthService;

    @Override
    public Long randomCreate(Long cantidad) {
        if (oAuthService.isAdministrador()) {
        this.create(new TipousuarioEntity("Administrador"));
        this.create(new TipousuarioEntity("Usuario"));
        return oTipousuarioRepository.count();
    } else {
        throw new UnauthorizedAccessException("No autorizado");
    } 
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
        if (oAuthService.isAdministrador()) {
        oTipousuarioRepository.deleteAll();
        return this.count();
        } else {
            throw new UnauthorizedAccessException("No autorizado");
        }
    }

}
