package com.padrinho.gameshow.service;

import com.padrinho.gameshow.domain.Edicao;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Edicao}.
 */
public interface EdicaoService {
    /**
     * Save a edicao.
     *
     * @param edicao the entity to save.
     * @return the persisted entity.
     */
    Edicao save(Edicao edicao);

    /**
     * Partially updates a edicao.
     *
     * @param edicao the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Edicao> partialUpdate(Edicao edicao);

    /**
     * Get all the edicaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Edicao> findAll(Pageable pageable);

    /**
     * Get the "id" edicao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Edicao> findOne(Long id);

    /**
     * Delete the "id" edicao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
