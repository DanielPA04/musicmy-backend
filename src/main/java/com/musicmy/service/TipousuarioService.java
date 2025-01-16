package com.musicmy.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicmy.entity.TipousuarioEntity;
import com.musicmy.repository.TipousuarioRepository;

@Service
public class TipousuarioService implements ServiceInterface<TipousuarioEntity> {

    @Autowired
    private TipousuarioRepository oTipousuarioRepository;

    @Autowired
    private RandomService oRandomService;

    @Override
    public Long randomCreate(Long cantidad) {

        this.create(new TipousuarioEntity("Administrador"));
        this.create(new TipousuarioEntity("Usuario"));

        return oTipousuarioRepository.count();
    }

    @Override
    public TipousuarioEntity randomSelection() {
        return oTipousuarioRepository.findAll()
                .get(oRandomService.getRandomInt(0, (int) oTipousuarioRepository.count() - 1));
    }

    @Override
    public Page<TipousuarioEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            // return oTipousuarioRepository
            // .findByNombreContainingOrGeneroContainingOrDescripcionContainingOrDiscograficaContaining(
            // filter.get(), filter.get(), filter.get(), filter.get(), oPageable);
            return oTipousuarioRepository.findAll(oPageable);
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
    public Long delete(Long id) {
        oTipousuarioRepository.deleteById(id);
        return 1L;
    }

    @Override
    public TipousuarioEntity create(TipousuarioEntity oTipousuarioEntity) {
        return oTipousuarioRepository.save(oTipousuarioEntity);
    }

    @Override
    public TipousuarioEntity update(TipousuarioEntity oTipousuarioEntity) {
        TipousuarioEntity oTipousuarioEntityFromDatabase = oTipousuarioRepository.findById(oTipousuarioEntity.getId())
                .get();
        if (oTipousuarioEntity.getNombre() != null) {
            oTipousuarioEntityFromDatabase.setNombre(oTipousuarioEntity.getNombre());
        }

        return oTipousuarioRepository.save(oTipousuarioEntityFromDatabase);

    }

    @Override
    public Long deleteAll() {
        oTipousuarioRepository.deleteAll();
        return this.count();
    }

}
