package com.padrinho.gameshow.service;

import com.padrinho.gameshow.domain.Participante;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Participante}.
 */
public interface ParticipanteService {
    /**
     * Save a participante.
     *
     * @param participante the entity to save.
     * @return the persisted entity.
     */
    Participante save(Participante participante);

    /**
     * Partially updates a participante.
     *
     * @param participante the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Participante> partialUpdate(Participante participante);

    /**
     * Get all the participantes.
     *
     * @return the list of entities.
     */
    List<Participante> findAll();

    /**
     * Get the "id" participante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Participante> findOne(Long id);

    /**
     * Delete the "id" participante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
