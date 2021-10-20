package com.padrinho.gameshow.web.rest;

import com.padrinho.gameshow.domain.Resposta;
import com.padrinho.gameshow.repository.RespostaRepository;
import com.padrinho.gameshow.service.RespostaService;
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
 * REST controller for managing {@link com.padrinho.gameshow.domain.Resposta}.
 */
@RestController
@RequestMapping("/api")
public class RespostaResource {

    private final Logger log = LoggerFactory.getLogger(RespostaResource.class);

    private static final String ENTITY_NAME = "resposta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RespostaService respostaService;

    private final RespostaRepository respostaRepository;

    public RespostaResource(RespostaService respostaService, RespostaRepository respostaRepository) {
        this.respostaService = respostaService;
        this.respostaRepository = respostaRepository;
    }

    /**
     * {@code POST  /respostas} : Create a new resposta.
     *
     * @param resposta the resposta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resposta, or with status {@code 400 (Bad Request)} if the resposta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/respostas")
    public ResponseEntity<Resposta> createResposta(@RequestBody Resposta resposta) throws URISyntaxException {
        log.debug("REST request to save Resposta : {}", resposta);
        if (resposta.getId() != null) {
            throw new BadRequestAlertException("A new resposta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Resposta result = respostaService.save(resposta);
        return ResponseEntity
            .created(new URI("/api/respostas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /respostas/:id} : Updates an existing resposta.
     *
     * @param id the id of the resposta to save.
     * @param resposta the resposta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resposta,
     * or with status {@code 400 (Bad Request)} if the resposta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resposta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/respostas/{id}")
    public ResponseEntity<Resposta> updateResposta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Resposta resposta
    ) throws URISyntaxException {
        log.debug("REST request to update Resposta : {}, {}", id, resposta);
        if (resposta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resposta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!respostaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Resposta result = respostaService.save(resposta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resposta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /respostas/:id} : Partial updates given fields of an existing resposta, field will ignore if it is null
     *
     * @param id the id of the resposta to save.
     * @param resposta the resposta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resposta,
     * or with status {@code 400 (Bad Request)} if the resposta is not valid,
     * or with status {@code 404 (Not Found)} if the resposta is not found,
     * or with status {@code 500 (Internal Server Error)} if the resposta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/respostas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Resposta> partialUpdateResposta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Resposta resposta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resposta partially : {}, {}", id, resposta);
        if (resposta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resposta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!respostaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Resposta> result = respostaService.partialUpdate(resposta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resposta.getId().toString())
        );
    }

    /**
     * {@code GET  /respostas} : get all the respostas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of respostas in body.
     */
    @GetMapping("/respostas")
    public List<Resposta> getAllRespostas() {
        log.debug("REST request to get all Respostas");
        return respostaService.findAll();
    }

    /**
     * {@code GET  /respostas/:id} : get the "id" resposta.
     *
     * @param id the id of the resposta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resposta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/respostas/{id}")
    public ResponseEntity<Resposta> getResposta(@PathVariable Long id) {
        log.debug("REST request to get Resposta : {}", id);
        Optional<Resposta> resposta = respostaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resposta);
    }

    /**
     * {@code DELETE  /respostas/:id} : delete the "id" resposta.
     *
     * @param id the id of the resposta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/respostas/{id}")
    public ResponseEntity<Void> deleteResposta(@PathVariable Long id) {
        log.debug("REST request to delete Resposta : {}", id);
        respostaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
