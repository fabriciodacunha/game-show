package com.padrinho.gameshow.service.impl;

import com.padrinho.gameshow.domain.Resposta;
import com.padrinho.gameshow.repository.RespostaRepository;
import com.padrinho.gameshow.service.RespostaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Resposta}.
 */
@Service
@Transactional
public class RespostaServiceImpl implements RespostaService {

    private final Logger log = LoggerFactory.getLogger(RespostaServiceImpl.class);

    private final RespostaRepository respostaRepository;

    public RespostaServiceImpl(RespostaRepository respostaRepository) {
        this.respostaRepository = respostaRepository;
    }

    @Override
    public Resposta save(Resposta resposta) {
        log.debug("Request to save Resposta : {}", resposta);
        return respostaRepository.save(resposta);
    }

    @Override
    public Optional<Resposta> partialUpdate(Resposta resposta) {
        log.debug("Request to partially update Resposta : {}", resposta);

        return respostaRepository
            .findById(resposta.getId())
            .map(existingResposta -> {
                if (resposta.getRespostaData() != null) {
                    existingResposta.setRespostaData(resposta.getRespostaData());
                }
                if (resposta.getRespostaAlternativa() != null) {
                    existingResposta.setRespostaAlternativa(resposta.getRespostaAlternativa());
                }
                if (resposta.getRespostaCerta() != null) {
                    existingResposta.setRespostaCerta(resposta.getRespostaCerta());
                }

                return existingResposta;
            })
            .map(respostaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resposta> findAll() {
        log.debug("Request to get all Respostas");
        return respostaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resposta> findOne(Long id) {
        log.debug("Request to get Resposta : {}", id);
        return respostaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resposta : {}", id);
        respostaRepository.deleteById(id);
    }
}
