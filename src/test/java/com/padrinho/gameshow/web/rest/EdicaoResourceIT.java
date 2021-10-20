package com.padrinho.gameshow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.padrinho.gameshow.IntegrationTest;
import com.padrinho.gameshow.domain.Edicao;
import com.padrinho.gameshow.repository.EdicaoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EdicaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EdicaoResourceIT {

    private static final String DEFAULT_EDICAO_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_EDICAO_TITULO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EDICAO_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EDICAO_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/edicaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EdicaoRepository edicaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEdicaoMockMvc;

    private Edicao edicao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edicao createEntity(EntityManager em) {
        Edicao edicao = new Edicao().edicaoTitulo(DEFAULT_EDICAO_TITULO).edicaoData(DEFAULT_EDICAO_DATA);
        return edicao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edicao createUpdatedEntity(EntityManager em) {
        Edicao edicao = new Edicao().edicaoTitulo(UPDATED_EDICAO_TITULO).edicaoData(UPDATED_EDICAO_DATA);
        return edicao;
    }

    @BeforeEach
    public void initTest() {
        edicao = createEntity(em);
    }

    @Test
    @Transactional
    void createEdicao() throws Exception {
        int databaseSizeBeforeCreate = edicaoRepository.findAll().size();
        // Create the Edicao
        restEdicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(edicao)))
            .andExpect(status().isCreated());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeCreate + 1);
        Edicao testEdicao = edicaoList.get(edicaoList.size() - 1);
        assertThat(testEdicao.getEdicaoTitulo()).isEqualTo(DEFAULT_EDICAO_TITULO);
        assertThat(testEdicao.getEdicaoData()).isEqualTo(DEFAULT_EDICAO_DATA);
    }

    @Test
    @Transactional
    void createEdicaoWithExistingId() throws Exception {
        // Create the Edicao with an existing ID
        edicao.setId(1L);

        int databaseSizeBeforeCreate = edicaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(edicao)))
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEdicaos() throws Exception {
        // Initialize the database
        edicaoRepository.saveAndFlush(edicao);

        // Get all the edicaoList
        restEdicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edicao.getId().intValue())))
            .andExpect(jsonPath("$.[*].edicaoTitulo").value(hasItem(DEFAULT_EDICAO_TITULO)))
            .andExpect(jsonPath("$.[*].edicaoData").value(hasItem(DEFAULT_EDICAO_DATA.toString())));
    }

    @Test
    @Transactional
    void getEdicao() throws Exception {
        // Initialize the database
        edicaoRepository.saveAndFlush(edicao);

        // Get the edicao
        restEdicaoMockMvc
            .perform(get(ENTITY_API_URL_ID, edicao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(edicao.getId().intValue()))
            .andExpect(jsonPath("$.edicaoTitulo").value(DEFAULT_EDICAO_TITULO))
            .andExpect(jsonPath("$.edicaoData").value(DEFAULT_EDICAO_DATA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEdicao() throws Exception {
        // Get the edicao
        restEdicaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEdicao() throws Exception {
        // Initialize the database
        edicaoRepository.saveAndFlush(edicao);

        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();

        // Update the edicao
        Edicao updatedEdicao = edicaoRepository.findById(edicao.getId()).get();
        // Disconnect from session so that the updates on updatedEdicao are not directly saved in db
        em.detach(updatedEdicao);
        updatedEdicao.edicaoTitulo(UPDATED_EDICAO_TITULO).edicaoData(UPDATED_EDICAO_DATA);

        restEdicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEdicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEdicao))
            )
            .andExpect(status().isOk());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
        Edicao testEdicao = edicaoList.get(edicaoList.size() - 1);
        assertThat(testEdicao.getEdicaoTitulo()).isEqualTo(UPDATED_EDICAO_TITULO);
        assertThat(testEdicao.getEdicaoData()).isEqualTo(UPDATED_EDICAO_DATA);
    }

    @Test
    @Transactional
    void putNonExistingEdicao() throws Exception {
        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();
        edicao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, edicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(edicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEdicao() throws Exception {
        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();
        edicao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(edicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEdicao() throws Exception {
        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();
        edicao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(edicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEdicaoWithPatch() throws Exception {
        // Initialize the database
        edicaoRepository.saveAndFlush(edicao);

        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();

        // Update the edicao using partial update
        Edicao partialUpdatedEdicao = new Edicao();
        partialUpdatedEdicao.setId(edicao.getId());

        partialUpdatedEdicao.edicaoData(UPDATED_EDICAO_DATA);

        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEdicao))
            )
            .andExpect(status().isOk());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
        Edicao testEdicao = edicaoList.get(edicaoList.size() - 1);
        assertThat(testEdicao.getEdicaoTitulo()).isEqualTo(DEFAULT_EDICAO_TITULO);
        assertThat(testEdicao.getEdicaoData()).isEqualTo(UPDATED_EDICAO_DATA);
    }

    @Test
    @Transactional
    void fullUpdateEdicaoWithPatch() throws Exception {
        // Initialize the database
        edicaoRepository.saveAndFlush(edicao);

        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();

        // Update the edicao using partial update
        Edicao partialUpdatedEdicao = new Edicao();
        partialUpdatedEdicao.setId(edicao.getId());

        partialUpdatedEdicao.edicaoTitulo(UPDATED_EDICAO_TITULO).edicaoData(UPDATED_EDICAO_DATA);

        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEdicao))
            )
            .andExpect(status().isOk());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
        Edicao testEdicao = edicaoList.get(edicaoList.size() - 1);
        assertThat(testEdicao.getEdicaoTitulo()).isEqualTo(UPDATED_EDICAO_TITULO);
        assertThat(testEdicao.getEdicaoData()).isEqualTo(UPDATED_EDICAO_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingEdicao() throws Exception {
        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();
        edicao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, edicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(edicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEdicao() throws Exception {
        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();
        edicao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(edicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEdicao() throws Exception {
        int databaseSizeBeforeUpdate = edicaoRepository.findAll().size();
        edicao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdicaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(edicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edicao in the database
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEdicao() throws Exception {
        // Initialize the database
        edicaoRepository.saveAndFlush(edicao);

        int databaseSizeBeforeDelete = edicaoRepository.findAll().size();

        // Delete the edicao
        restEdicaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, edicao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Edicao> edicaoList = edicaoRepository.findAll();
        assertThat(edicaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
