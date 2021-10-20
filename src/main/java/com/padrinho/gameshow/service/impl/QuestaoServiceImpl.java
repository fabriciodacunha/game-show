package com.padrinho.gameshow.service.impl;

import com.padrinho.gameshow.domain.Questao;
import com.padrinho.gameshow.repository.QuestaoRepository;
import com.padrinho.gameshow.service.QuestaoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Questao}.
 */
@Service
@Transactional
public class QuestaoServiceImpl implements QuestaoService {

    private final Logger log = LoggerFactory.getLogger(QuestaoServiceImpl.class);

    private final QuestaoRepository questaoRepository;

    public QuestaoServiceImpl(QuestaoRepository questaoRepository) {
        this.questaoRepository = questaoRepository;
    }

    @Override
    public Questao save(Questao questao) {
        log.debug("Request to save Questao : {}", questao);
        return questaoRepository.save(questao);
    }

    @Override
    public Optional<Questao> partialUpdate(Questao questao) {
        log.debug("Request to partially update Questao : {}", questao);

        return questaoRepository
            .findById(questao.getId())
            .map(existingQuestao -> {
                if (questao.getQuestaoTitulo() != null) {
                    existingQuestao.setQuestaoTitulo(questao.getQuestaoTitulo());
                }
                if (questao.getAlternativaA() != null) {
                    existingQuestao.setAlternativaA(questao.getAlternativaA());
                }
                if (questao.getAlternativaB() != null) {
                    existingQuestao.setAlternativaB(questao.getAlternativaB());
                }
                if (questao.getAlternativaC() != null) {
                    existingQuestao.setAlternativaC(questao.getAlternativaC());
                }
                if (questao.getAlternativaD() != null) {
                    existingQuestao.setAlternativaD(questao.getAlternativaD());
                }
                if (questao.getNivelIdade() != null) {
                    existingQuestao.setNivelIdade(questao.getNivelIdade());
                }
                if (questao.getAssunto() != null) {
                    existingQuestao.setAssunto(questao.getAssunto());
                }
                if (questao.getAlternativaCerta() != null) {
                    existingQuestao.setAlternativaCerta(questao.getAlternativaCerta());
                }

                return existingQuestao;
            })
            .map(questaoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Questao> findAll(Pageable pageable) {
        log.debug("Request to get all Questaos");
        return questaoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Questao> findOne(Long id) {
        log.debug("Request to get Questao : {}", id);
        return questaoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Questao : {}", id);
        questaoRepository.deleteById(id);
    }
}
