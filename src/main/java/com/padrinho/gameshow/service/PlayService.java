package com.padrinho.gameshow.service;

import com.padrinho.gameshow.domain.Play;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Play}.
 */
public interface PlayService {
    /**
     * Save a play.
     *
     * @param play the entity to save.
     * @return the persisted entity.
     */
    Play save(Play play);

    /**
     * Partially updates a play.
     *
     * @param play the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Play> partialUpdate(Play play);

    /**
     * Get all the plays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Play> findAll(Pageable pageable);
    /**
     * Get all the Play where Edicao is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Play> findAllWhereEdicaoIsNull();

    /**
     * Get the "id" play.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Play> findOne(Long id);

    /**
     * Delete the "id" play.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
