package com.padrinho.gameshow.web.rest;

import static com.padrinho.gameshow.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.padrinho.gameshow.IntegrationTest;
import com.padrinho.gameshow.domain.Play;
import com.padrinho.gameshow.repository.PlayRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayResourceIT {

    private static final ZonedDateTime DEFAULT_PLAY_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PLAY_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/plays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayRepository playRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayMockMvc;

    private Play play;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Play createEntity(EntityManager em) {
        Play play = new Play().playData(DEFAULT_PLAY_DATA);
        return play;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Play createUpdatedEntity(EntityManager em) {
        Play play = new Play().playData(UPDATED_PLAY_DATA);
        return play;
    }

    @BeforeEach
    public void initTest() {
        play = createEntity(em);
    }

    @Test
    @Transactional
    void createPlay() throws Exception {
        int databaseSizeBeforeCreate = playRepository.findAll().size();
        // Create the Play
        restPlayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(play)))
            .andExpect(status().isCreated());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeCreate + 1);
        Play testPlay = playList.get(playList.size() - 1);
        assertThat(testPlay.getPlayData()).isEqualTo(DEFAULT_PLAY_DATA);
    }

    @Test
    @Transactional
    void createPlayWithExistingId() throws Exception {
        // Create the Play with an existing ID
        play.setId(1L);

        int databaseSizeBeforeCreate = playRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(play)))
            .andExpect(status().isBadRequest());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlays() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        // Get all the playList
        restPlayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(play.getId().intValue())))
            .andExpect(jsonPath("$.[*].playData").value(hasItem(sameInstant(DEFAULT_PLAY_DATA))));
    }

    @Test
    @Transactional
    void getPlay() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        // Get the play
        restPlayMockMvc
            .perform(get(ENTITY_API_URL_ID, play.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(play.getId().intValue()))
            .andExpect(jsonPath("$.playData").value(sameInstant(DEFAULT_PLAY_DATA)));
    }

    @Test
    @Transactional
    void getNonExistingPlay() throws Exception {
        // Get the play
        restPlayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlay() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        int databaseSizeBeforeUpdate = playRepository.findAll().size();

        // Update the play
        Play updatedPlay = playRepository.findById(play.getId()).get();
        // Disconnect from session so that the updates on updatedPlay are not directly saved in db
        em.detach(updatedPlay);
        updatedPlay.playData(UPDATED_PLAY_DATA);

        restPlayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlay))
            )
            .andExpect(status().isOk());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
        Play testPlay = playList.get(playList.size() - 1);
        assertThat(testPlay.getPlayData()).isEqualTo(UPDATED_PLAY_DATA);
    }

    @Test
    @Transactional
    void putNonExistingPlay() throws Exception {
        int databaseSizeBeforeUpdate = playRepository.findAll().size();
        play.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, play.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(play))
            )
            .andExpect(status().isBadRequest());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlay() throws Exception {
        int databaseSizeBeforeUpdate = playRepository.findAll().size();
        play.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(play))
            )
            .andExpect(status().isBadRequest());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlay() throws Exception {
        int databaseSizeBeforeUpdate = playRepository.findAll().size();
        play.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(play)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayWithPatch() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        int databaseSizeBeforeUpdate = playRepository.findAll().size();

        // Update the play using partial update
        Play partialUpdatedPlay = new Play();
        partialUpdatedPlay.setId(play.getId());

        restPlayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlay))
            )
            .andExpect(status().isOk());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
        Play testPlay = playList.get(playList.size() - 1);
        assertThat(testPlay.getPlayData()).isEqualTo(DEFAULT_PLAY_DATA);
    }

    @Test
    @Transactional
    void fullUpdatePlayWithPatch() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        int databaseSizeBeforeUpdate = playRepository.findAll().size();

        // Update the play using partial update
        Play partialUpdatedPlay = new Play();
        partialUpdatedPlay.setId(play.getId());

        partialUpdatedPlay.playData(UPDATED_PLAY_DATA);

        restPlayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlay))
            )
            .andExpect(status().isOk());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
        Play testPlay = playList.get(playList.size() - 1);
        assertThat(testPlay.getPlayData()).isEqualTo(UPDATED_PLAY_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingPlay() throws Exception {
        int databaseSizeBeforeUpdate = playRepository.findAll().size();
        play.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, play.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(play))
            )
            .andExpect(status().isBadRequest());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlay() throws Exception {
        int databaseSizeBeforeUpdate = playRepository.findAll().size();
        play.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(play))
            )
            .andExpect(status().isBadRequest());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlay() throws Exception {
        int databaseSizeBeforeUpdate = playRepository.findAll().size();
        play.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(play)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlay() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        int databaseSizeBeforeDelete = playRepository.findAll().size();

        // Delete the play
        restPlayMockMvc
            .perform(delete(ENTITY_API_URL_ID, play.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
