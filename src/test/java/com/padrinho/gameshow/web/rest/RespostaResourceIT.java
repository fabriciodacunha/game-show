package com.padrinho.gameshow.web.rest;

import static com.padrinho.gameshow.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.padrinho.gameshow.IntegrationTest;
import com.padrinho.gameshow.domain.Resposta;
import com.padrinho.gameshow.domain.enumeration.Alternativa;
import com.padrinho.gameshow.repository.RespostaRepository;
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
 * Integration tests for the {@link RespostaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RespostaResourceIT {

    private static final ZonedDateTime DEFAULT_RESPOSTA_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESPOSTA_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Alternativa DEFAULT_RESPOSTA_ALTERNATIVA = Alternativa.A;
    private static final Alternativa UPDATED_RESPOSTA_ALTERNATIVA = Alternativa.B;

    private static final Boolean DEFAULT_RESPOSTA_CERTA = false;
    private static final Boolean UPDATED_RESPOSTA_CERTA = true;

    private static final String ENTITY_API_URL = "/api/respostas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRespostaMockMvc;

    private Resposta resposta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resposta createEntity(EntityManager em) {
        Resposta resposta = new Resposta()
            .respostaData(DEFAULT_RESPOSTA_DATA)
            .respostaAlternativa(DEFAULT_RESPOSTA_ALTERNATIVA)
            .respostaCerta(DEFAULT_RESPOSTA_CERTA);
        return resposta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resposta createUpdatedEntity(EntityManager em) {
        Resposta resposta = new Resposta()
            .respostaData(UPDATED_RESPOSTA_DATA)
            .respostaAlternativa(UPDATED_RESPOSTA_ALTERNATIVA)
            .respostaCerta(UPDATED_RESPOSTA_CERTA);
        return resposta;
    }

    @BeforeEach
    public void initTest() {
        resposta = createEntity(em);
    }

    @Test
    @Transactional
    void createResposta() throws Exception {
        int databaseSizeBeforeCreate = respostaRepository.findAll().size();
        // Create the Resposta
        restRespostaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resposta)))
            .andExpect(status().isCreated());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeCreate + 1);
        Resposta testResposta = respostaList.get(respostaList.size() - 1);
        assertThat(testResposta.getRespostaData()).isEqualTo(DEFAULT_RESPOSTA_DATA);
        assertThat(testResposta.getRespostaAlternativa()).isEqualTo(DEFAULT_RESPOSTA_ALTERNATIVA);
        assertThat(testResposta.getRespostaCerta()).isEqualTo(DEFAULT_RESPOSTA_CERTA);
    }

    @Test
    @Transactional
    void createRespostaWithExistingId() throws Exception {
        // Create the Resposta with an existing ID
        resposta.setId(1L);

        int databaseSizeBeforeCreate = respostaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRespostaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resposta)))
            .andExpect(status().isBadRequest());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRespostas() throws Exception {
        // Initialize the database
        respostaRepository.saveAndFlush(resposta);

        // Get all the respostaList
        restRespostaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resposta.getId().intValue())))
            .andExpect(jsonPath("$.[*].respostaData").value(hasItem(sameInstant(DEFAULT_RESPOSTA_DATA))))
            .andExpect(jsonPath("$.[*].respostaAlternativa").value(hasItem(DEFAULT_RESPOSTA_ALTERNATIVA.toString())))
            .andExpect(jsonPath("$.[*].respostaCerta").value(hasItem(DEFAULT_RESPOSTA_CERTA.booleanValue())));
    }

    @Test
    @Transactional
    void getResposta() throws Exception {
        // Initialize the database
        respostaRepository.saveAndFlush(resposta);

        // Get the resposta
        restRespostaMockMvc
            .perform(get(ENTITY_API_URL_ID, resposta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resposta.getId().intValue()))
            .andExpect(jsonPath("$.respostaData").value(sameInstant(DEFAULT_RESPOSTA_DATA)))
            .andExpect(jsonPath("$.respostaAlternativa").value(DEFAULT_RESPOSTA_ALTERNATIVA.toString()))
            .andExpect(jsonPath("$.respostaCerta").value(DEFAULT_RESPOSTA_CERTA.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingResposta() throws Exception {
        // Get the resposta
        restRespostaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResposta() throws Exception {
        // Initialize the database
        respostaRepository.saveAndFlush(resposta);

        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();

        // Update the resposta
        Resposta updatedResposta = respostaRepository.findById(resposta.getId()).get();
        // Disconnect from session so that the updates on updatedResposta are not directly saved in db
        em.detach(updatedResposta);
        updatedResposta
            .respostaData(UPDATED_RESPOSTA_DATA)
            .respostaAlternativa(UPDATED_RESPOSTA_ALTERNATIVA)
            .respostaCerta(UPDATED_RESPOSTA_CERTA);

        restRespostaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResposta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResposta))
            )
            .andExpect(status().isOk());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
        Resposta testResposta = respostaList.get(respostaList.size() - 1);
        assertThat(testResposta.getRespostaData()).isEqualTo(UPDATED_RESPOSTA_DATA);
        assertThat(testResposta.getRespostaAlternativa()).isEqualTo(UPDATED_RESPOSTA_ALTERNATIVA);
        assertThat(testResposta.getRespostaCerta()).isEqualTo(UPDATED_RESPOSTA_CERTA);
    }

    @Test
    @Transactional
    void putNonExistingResposta() throws Exception {
        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();
        resposta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespostaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resposta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resposta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResposta() throws Exception {
        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();
        resposta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resposta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResposta() throws Exception {
        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();
        resposta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resposta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRespostaWithPatch() throws Exception {
        // Initialize the database
        respostaRepository.saveAndFlush(resposta);

        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();

        // Update the resposta using partial update
        Resposta partialUpdatedResposta = new Resposta();
        partialUpdatedResposta.setId(resposta.getId());

        partialUpdatedResposta.respostaCerta(UPDATED_RESPOSTA_CERTA);

        restRespostaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResposta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResposta))
            )
            .andExpect(status().isOk());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
        Resposta testResposta = respostaList.get(respostaList.size() - 1);
        assertThat(testResposta.getRespostaData()).isEqualTo(DEFAULT_RESPOSTA_DATA);
        assertThat(testResposta.getRespostaAlternativa()).isEqualTo(DEFAULT_RESPOSTA_ALTERNATIVA);
        assertThat(testResposta.getRespostaCerta()).isEqualTo(UPDATED_RESPOSTA_CERTA);
    }

    @Test
    @Transactional
    void fullUpdateRespostaWithPatch() throws Exception {
        // Initialize the database
        respostaRepository.saveAndFlush(resposta);

        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();

        // Update the resposta using partial update
        Resposta partialUpdatedResposta = new Resposta();
        partialUpdatedResposta.setId(resposta.getId());

        partialUpdatedResposta
            .respostaData(UPDATED_RESPOSTA_DATA)
            .respostaAlternativa(UPDATED_RESPOSTA_ALTERNATIVA)
            .respostaCerta(UPDATED_RESPOSTA_CERTA);

        restRespostaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResposta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResposta))
            )
            .andExpect(status().isOk());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
        Resposta testResposta = respostaList.get(respostaList.size() - 1);
        assertThat(testResposta.getRespostaData()).isEqualTo(UPDATED_RESPOSTA_DATA);
        assertThat(testResposta.getRespostaAlternativa()).isEqualTo(UPDATED_RESPOSTA_ALTERNATIVA);
        assertThat(testResposta.getRespostaCerta()).isEqualTo(UPDATED_RESPOSTA_CERTA);
    }

    @Test
    @Transactional
    void patchNonExistingResposta() throws Exception {
        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();
        resposta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespostaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resposta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resposta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResposta() throws Exception {
        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();
        resposta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resposta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResposta() throws Exception {
        int databaseSizeBeforeUpdate = respostaRepository.findAll().size();
        resposta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resposta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resposta in the database
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResposta() throws Exception {
        // Initialize the database
        respostaRepository.saveAndFlush(resposta);

        int databaseSizeBeforeDelete = respostaRepository.findAll().size();

        // Delete the resposta
        restRespostaMockMvc
            .perform(delete(ENTITY_API_URL_ID, resposta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resposta> respostaList = respostaRepository.findAll();
        assertThat(respostaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
