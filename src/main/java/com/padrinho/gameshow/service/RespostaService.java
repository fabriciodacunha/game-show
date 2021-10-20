package com.padrinho.gameshow.service;

import com.padrinho.gameshow.domain.Resposta;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Resposta}.
 */
public interface RespostaService {
    /**
     * Save a resposta.
     *
     * @param resposta the entity to save.
     * @return the persisted entity.
     */
    Resposta save(Resposta resposta);

    /**
     * Partially updates a resposta.
     *
     * @param resposta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Resposta> partialUpdate(Resposta resposta);

    /**
     * Get all the respostas.
     *
     * @return the list of entities.
     */
    List<Resposta> findAll();

    /**
     * Get the "id" resposta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Resposta> findOne(Long id);

    /**
     * Delete the "id" resposta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
