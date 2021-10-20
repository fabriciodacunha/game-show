package com.padrinho.gameshow.service;

import com.padrinho.gameshow.domain.Questao;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Questao}.
 */
public interface QuestaoService {
    /**
     * Save a questao.
     *
     * @param questao the entity to save.
     * @return the persisted entity.
     */
    Questao save(Questao questao);

    /**
     * Partially updates a questao.
     *
     * @param questao the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Questao> partialUpdate(Questao questao);

    /**
     * Get all the questaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Questao> findAll(Pageable pageable);

    /**
     * Get the "id" questao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Questao> findOne(Long id);

    /**
     * Delete the "id" questao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
