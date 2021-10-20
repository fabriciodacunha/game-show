package com.padrinho.gameshow.web.rest;

import com.padrinho.gameshow.domain.Participante;
import com.padrinho.gameshow.repository.ParticipanteRepository;
import com.padrinho.gameshow.service.ParticipanteService;
import com.padrinho.gameshow.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.padrinho.gameshow.domain.Participante}.
 */
@RestController
@RequestMapping("/api")
public class ParticipanteResource {

    private final Logger log = LoggerFactory.getLogger(ParticipanteResource.class);

    private static final String ENTITY_NAME = "participante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipanteService participanteService;

    private final ParticipanteRepository participanteRepository;

    public ParticipanteResource(ParticipanteService participanteService, ParticipanteRepository participanteRepository) {
        this.participanteService = participanteService;
        this.participanteRepository = participanteRepository;
    }

    /**
     * {@code POST  /participantes} : Create a new participante.
     *
     * @param participante the participante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new participante, or with status {@code 400 (Bad Request)} if the participante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/participantes")
    public ResponseEntity<Participante> createParticipante(@RequestBody Participante participante) throws URISyntaxException {
        log.debug("REST request to save Participante : {}", participante);
        if (participante.getId() != null) {
            throw new BadRequestAlertException("A new participante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Participante result = participanteService.save(participante);
        return ResponseEntity
            .created(new URI("/api/participantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /participantes/:id} : Updates an existing participante.
     *
     * @param id the id of the participante to save.
     * @param participante the participante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participante,
     * or with status {@code 400 (Bad Request)} if the participante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the participante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/participantes/{id}")
    public ResponseEntity<Participante> updateParticipante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Participante participante
    ) throws URISyntaxException {
        log.debug("REST request to update Participante : {}, {}", id, participante);
        if (participante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, participante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!participanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Participante result = participanteService.save(participante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, participante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /participantes/:id} : Partial updates given fields of an existing participante, field will ignore if it is null
     *
     * @param id the id of the participante to save.
     * @param participante the participante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participante,
     * or with status {@code 400 (Bad Request)} if the participante is not valid,
     * or with status {@code 404 (Not Found)} if the participante is not found,
     * or with status {@code 500 (Internal Server Error)} if the participante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/participantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Participante> partialUpdateParticipante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Participante participante
    ) throws URISyntaxException {
        log.debug("REST request to partial update Participante partially : {}, {}", id, participante);
        if (participante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, participante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!participanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Participante> result = participanteService.partialUpdate(participante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, participante.getId().toString())
        );
    }

    /**
     * {@code GET  /participantes} : get all the participantes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of participantes in body.
     */
    @GetMapping("/participantes")
    public List<Participante> getAllParticipantes() {
        log.debug("REST request to get all Participantes");
        return participanteService.findAll();
    }

    /**
     * {@code GET  /participantes/:id} : get the "id" participante.
     *
     * @param id the id of the participante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the participante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/participantes/{id}")
    public ResponseEntity<Participante> getParticipante(@PathVariable Long id) {
        log.debug("REST request to get Participante : {}", id);
        Optional<Participante> participante = participanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(participante);
    }

    /**
     * {@code DELETE  /participantes/:id} : delete the "id" participante.
     *
     * @param id the id of the participante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/participantes/{id}")
    public ResponseEntity<Void> deleteParticipante(@PathVariable Long id) {
        log.debug("REST request to delete Participante : {}", id);
        participanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
