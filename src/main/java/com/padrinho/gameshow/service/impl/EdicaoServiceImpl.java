package com.padrinho.gameshow.service.impl;

import com.padrinho.gameshow.domain.Edicao;
import com.padrinho.gameshow.repository.EdicaoRepository;
import com.padrinho.gameshow.service.EdicaoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Edicao}.
 */
@Service
@Transactional
public class EdicaoServiceImpl implements EdicaoService {

    private final Logger log = LoggerFactory.getLogger(EdicaoServiceImpl.class);

    private final EdicaoRepository edicaoRepository;

    public EdicaoServiceImpl(EdicaoRepository edicaoRepository) {
        this.edicaoRepository = edicaoRepository;
    }

    @Override
    public Edicao save(Edicao edicao) {
        log.debug("Request to save Edicao : {}", edicao);
        return edicaoRepository.save(edicao);
    }

    @Override
    public Optional<Edicao> partialUpdate(Edicao edicao) {
        log.debug("Request to partially update Edicao : {}", edicao);

        return edicaoRepository
            .findById(edicao.getId())
            .map(existingEdicao -> {
                if (edicao.getEdicaoTitulo() != null) {
                    existingEdicao.setEdicaoTitulo(edicao.getEdicaoTitulo());
                }
                if (edicao.getEdicaoData() != null) {
                    existingEdicao.setEdicaoData(edicao.getEdicaoData());
                }

                return existingEdicao;
            })
            .map(edicaoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Edicao> findAll(Pageable pageable) {
        log.debug("Request to get all Edicaos");
        return edicaoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Edicao> findOne(Long id) {
        log.debug("Request to get Edicao : {}", id);
        return edicaoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Edicao : {}", id);
        edicaoRepository.deleteById(id);
    }
}
