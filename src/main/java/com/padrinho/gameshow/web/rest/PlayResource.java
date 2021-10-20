package com.padrinho.gameshow.web.rest;

import com.padrinho.gameshow.domain.Play;
import com.padrinho.gameshow.repository.PlayRepository;
import com.padrinho.gameshow.service.PlayService;
import com.padrinho.gameshow.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.padrinho.gameshow.domain.Play}.
 */
@RestController
@RequestMapping("/api")
public class PlayResource {

    private final Logger log = LoggerFactory.getLogger(PlayResource.class);

    private static final String ENTITY_NAME = "play";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayService playService;

    private final PlayRepository playRepository;

    public PlayResource(PlayService playService, PlayRepository playRepository) {
        this.playService = playService;
        this.playRepository = playRepository;
    }

    /**
     * {@code POST  /plays} : Create a new play.
     *
     * @param play the play to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new play, or with status {@code 400 (Bad Request)} if the play has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plays")
    public ResponseEntity<Play> createPlay(@RequestBody Play play) throws URISyntaxException {
        log.debug("REST request to save Play : {}", play);
        if (play.getId() != null) {
            throw new BadRequestAlertException("A new play cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Play result = playService.save(play);
        return ResponseEntity
            .created(new URI("/api/plays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plays/:id} : Updates an existing play.
     *
     * @param id the id of the play to save.
     * @param play the play to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated play,
     * or with status {@code 400 (Bad Request)} if the play is not valid,
     * or with status {@code 500 (Internal Server Error)} if the play couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plays/{id}")
    public ResponseEntity<Play> updatePlay(@PathVariable(value = "id", required = false) final Long id, @RequestBody Play play)
        throws URISyntaxException {
        log.debug("REST request to update Play : {}, {}", id, play);
        if (play.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, play.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Play result = playService.save(play);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, play.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plays/:id} : Partial updates given fields of an existing play, field will ignore if it is null
     *
     * @param id the id of the play to save.
     * @param play the play to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated play,
     * or with status {@code 400 (Bad Request)} if the play is not valid,
     * or with status {@code 404 (Not Found)} if the play is not found,
     * or with status {@code 500 (Internal Server Error)} if the play couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Play> partialUpdatePlay(@PathVariable(value = "id", required = false) final Long id, @RequestBody Play play)
        throws URISyntaxException {
        log.debug("REST request to partial update Play partially : {}, {}", id, play);
        if (play.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, play.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Play> result = playService.partialUpdate(play);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, play.getId().toString())
        );
    }

    /**
     * {@code GET  /plays} : get all the plays.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plays in body.
     */
    @GetMapping("/plays")
    public ResponseEntity<List<Play>> getAllPlays(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("edicao-is-null".equals(filter)) {
            log.debug("REST request to get all Plays where edicao is null");
            return new ResponseEntity<>(playService.findAllWhereEdicaoIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Plays");
        Page<Play> page = playService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plays/:id} : get the "id" play.
     *
     * @param id the id of the play to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the play, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plays/{id}")
    public ResponseEntity<Play> getPlay(@PathVariable Long id) {
        log.debug("REST request to get Play : {}", id);
        Optional<Play> play = playService.findOne(id);
        return ResponseUtil.wrapOrNotFound(play);
    }

    /**
     * {@code DELETE  /plays/:id} : delete the "id" play.
     *
     * @param id the id of the play to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plays/{id}")
    public ResponseEntity<Void> deletePlay(@PathVariable Long id) {
        log.debug("REST request to delete Play : {}", id);
        playService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
