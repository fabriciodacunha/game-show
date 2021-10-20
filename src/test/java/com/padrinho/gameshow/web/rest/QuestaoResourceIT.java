package com.padrinho.gameshow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.padrinho.gameshow.IntegrationTest;
import com.padrinho.gameshow.domain.Questao;
import com.padrinho.gameshow.domain.enumeration.Alternativa;
import com.padrinho.gameshow.domain.enumeration.Assunto;
import com.padrinho.gameshow.repository.QuestaoRepository;
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
 * Integration tests for the {@link QuestaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestaoResourceIT {

    private static final String DEFAULT_QUESTAO_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_QUESTAO_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATIVA_A = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATIVA_A = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATIVA_B = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATIVA_B = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATIVA_C = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATIVA_C = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATIVA_D = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATIVA_D = "BBBBBBBBBB";

    private static final Integer DEFAULT_NIVEL_IDADE = 1;
    private static final Integer UPDATED_NIVEL_IDADE = 2;

    private static final Assunto DEFAULT_ASSUNTO = Assunto.PORTUGUES;
    private static final Assunto UPDATED_ASSUNTO = Assunto.INGLES;

    private static final Alternativa DEFAULT_ALTERNATIVA_CERTA = Alternativa.A;
    private static final Alternativa UPDATED_ALTERNATIVA_CERTA = Alternativa.B;

    private static final String ENTITY_API_URL = "/api/questaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestaoMockMvc;

    private Questao questao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questao createEntity(EntityManager em) {
        Questao questao = new Questao()
            .questaoTitulo(DEFAULT_QUESTAO_TITULO)
            .alternativaA(DEFAULT_ALTERNATIVA_A)
            .alternativaB(DEFAULT_ALTERNATIVA_B)
            .alternativaC(DEFAULT_ALTERNATIVA_C)
            .alternativaD(DEFAULT_ALTERNATIVA_D)
            .nivelIdade(DEFAULT_NIVEL_IDADE)
            .assunto(DEFAULT_ASSUNTO)
            .alternativaCerta(DEFAULT_ALTERNATIVA_CERTA);
        return questao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questao createUpdatedEntity(EntityManager em) {
        Questao questao = new Questao()
            .questaoTitulo(UPDATED_QUESTAO_TITULO)
            .alternativaA(UPDATED_ALTERNATIVA_A)
            .alternativaB(UPDATED_ALTERNATIVA_B)
            .alternativaC(UPDATED_ALTERNATIVA_C)
            .alternativaD(UPDATED_ALTERNATIVA_D)
            .nivelIdade(UPDATED_NIVEL_IDADE)
            .assunto(UPDATED_ASSUNTO)
            .alternativaCerta(UPDATED_ALTERNATIVA_CERTA);
        return questao;
    }

    @BeforeEach
    public void initTest() {
        questao = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestao() throws Exception {
        int databaseSizeBeforeCreate = questaoRepository.findAll().size();
        // Create the Questao
        restQuestaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questao)))
            .andExpect(status().isCreated());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeCreate + 1);
        Questao testQuestao = questaoList.get(questaoList.size() - 1);
        assertThat(testQuestao.getQuestaoTitulo()).isEqualTo(DEFAULT_QUESTAO_TITULO);
        assertThat(testQuestao.getAlternativaA()).isEqualTo(DEFAULT_ALTERNATIVA_A);
        assertThat(testQuestao.getAlternativaB()).isEqualTo(DEFAULT_ALTERNATIVA_B);
        assertThat(testQuestao.getAlternativaC()).isEqualTo(DEFAULT_ALTERNATIVA_C);
        assertThat(testQuestao.getAlternativaD()).isEqualTo(DEFAULT_ALTERNATIVA_D);
        assertThat(testQuestao.getNivelIdade()).isEqualTo(DEFAULT_NIVEL_IDADE);
        assertThat(testQuestao.getAssunto()).isEqualTo(DEFAULT_ASSUNTO);
        assertThat(testQuestao.getAlternativaCerta()).isEqualTo(DEFAULT_ALTERNATIVA_CERTA);
    }

    @Test
    @Transactional
    void createQuestaoWithExistingId() throws Exception {
        // Create the Questao with an existing ID
        questao.setId(1L);

        int databaseSizeBeforeCreate = questaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questao)))
            .andExpect(status().isBadRequest());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestaos() throws Exception {
        // Initialize the database
        questaoRepository.saveAndFlush(questao);

        // Get all the questaoList
        restQuestaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questao.getId().intValue())))
            .andExpect(jsonPath("$.[*].questaoTitulo").value(hasItem(DEFAULT_QUESTAO_TITULO)))
            .andExpect(jsonPath("$.[*].alternativaA").value(hasItem(DEFAULT_ALTERNATIVA_A)))
            .andExpect(jsonPath("$.[*].alternativaB").value(hasItem(DEFAULT_ALTERNATIVA_B)))
            .andExpect(jsonPath("$.[*].alternativaC").value(hasItem(DEFAULT_ALTERNATIVA_C)))
            .andExpect(jsonPath("$.[*].alternativaD").value(hasItem(DEFAULT_ALTERNATIVA_D)))
            .andExpect(jsonPath("$.[*].nivelIdade").value(hasItem(DEFAULT_NIVEL_IDADE)))
            .andExpect(jsonPath("$.[*].assunto").value(hasItem(DEFAULT_ASSUNTO.toString())))
            .andExpect(jsonPath("$.[*].alternativaCerta").value(hasItem(DEFAULT_ALTERNATIVA_CERTA.toString())));
    }

    @Test
    @Transactional
    void getQuestao() throws Exception {
        // Initialize the database
        questaoRepository.saveAndFlush(questao);

        // Get the questao
        restQuestaoMockMvc
            .perform(get(ENTITY_API_URL_ID, questao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questao.getId().intValue()))
            .andExpect(jsonPath("$.questaoTitulo").value(DEFAULT_QUESTAO_TITULO))
            .andExpect(jsonPath("$.alternativaA").value(DEFAULT_ALTERNATIVA_A))
            .andExpect(jsonPath("$.alternativaB").value(DEFAULT_ALTERNATIVA_B))
            .andExpect(jsonPath("$.alternativaC").value(DEFAULT_ALTERNATIVA_C))
            .andExpect(jsonPath("$.alternativaD").value(DEFAULT_ALTERNATIVA_D))
            .andExpect(jsonPath("$.nivelIdade").value(DEFAULT_NIVEL_IDADE))
            .andExpect(jsonPath("$.assunto").value(DEFAULT_ASSUNTO.toString()))
            .andExpect(jsonPath("$.alternativaCerta").value(DEFAULT_ALTERNATIVA_CERTA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuestao() throws Exception {
        // Get the questao
        restQuestaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestao() throws Exception {
        // Initialize the database
        questaoRepository.saveAndFlush(questao);

        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();

        // Update the questao
        Questao updatedQuestao = questaoRepository.findById(questao.getId()).get();
        // Disconnect from session so that the updates on updatedQuestao are not directly saved in db
        em.detach(updatedQuestao);
        updatedQuestao
            .questaoTitulo(UPDATED_QUESTAO_TITULO)
            .alternativaA(UPDATED_ALTERNATIVA_A)
            .alternativaB(UPDATED_ALTERNATIVA_B)
            .alternativaC(UPDATED_ALTERNATIVA_C)
            .alternativaD(UPDATED_ALTERNATIVA_D)
            .nivelIdade(UPDATED_NIVEL_IDADE)
            .assunto(UPDATED_ASSUNTO)
            .alternativaCerta(UPDATED_ALTERNATIVA_CERTA);

        restQuestaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestao))
            )
            .andExpect(status().isOk());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
        Questao testQuestao = questaoList.get(questaoList.size() - 1);
        assertThat(testQuestao.getQuestaoTitulo()).isEqualTo(UPDATED_QUESTAO_TITULO);
        assertThat(testQuestao.getAlternativaA()).isEqualTo(UPDATED_ALTERNATIVA_A);
        assertThat(testQuestao.getAlternativaB()).isEqualTo(UPDATED_ALTERNATIVA_B);
        assertThat(testQuestao.getAlternativaC()).isEqualTo(UPDATED_ALTERNATIVA_C);
        assertThat(testQuestao.getAlternativaD()).isEqualTo(UPDATED_ALTERNATIVA_D);
        assertThat(testQuestao.getNivelIdade()).isEqualTo(UPDATED_NIVEL_IDADE);
        assertThat(testQuestao.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testQuestao.getAlternativaCerta()).isEqualTo(UPDATED_ALTERNATIVA_CERTA);
    }

    @Test
    @Transactional
    void putNonExistingQuestao() throws Exception {
        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();
        questao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestao() throws Exception {
        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();
        questao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestao() throws Exception {
        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();
        questao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestaoWithPatch() throws Exception {
        // Initialize the database
        questaoRepository.saveAndFlush(questao);

        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();

        // Update the questao using partial update
        Questao partialUpdatedQuestao = new Questao();
        partialUpdatedQuestao.setId(questao.getId());

        partialUpdatedQuestao
            .questaoTitulo(UPDATED_QUESTAO_TITULO)
            .alternativaC(UPDATED_ALTERNATIVA_C)
            .alternativaCerta(UPDATED_ALTERNATIVA_CERTA);

        restQuestaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestao))
            )
            .andExpect(status().isOk());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
        Questao testQuestao = questaoList.get(questaoList.size() - 1);
        assertThat(testQuestao.getQuestaoTitulo()).isEqualTo(UPDATED_QUESTAO_TITULO);
        assertThat(testQuestao.getAlternativaA()).isEqualTo(DEFAULT_ALTERNATIVA_A);
        assertThat(testQuestao.getAlternativaB()).isEqualTo(DEFAULT_ALTERNATIVA_B);
        assertThat(testQuestao.getAlternativaC()).isEqualTo(UPDATED_ALTERNATIVA_C);
        assertThat(testQuestao.getAlternativaD()).isEqualTo(DEFAULT_ALTERNATIVA_D);
        assertThat(testQuestao.getNivelIdade()).isEqualTo(DEFAULT_NIVEL_IDADE);
        assertThat(testQuestao.getAssunto()).isEqualTo(DEFAULT_ASSUNTO);
        assertThat(testQuestao.getAlternativaCerta()).isEqualTo(UPDATED_ALTERNATIVA_CERTA);
    }

    @Test
    @Transactional
    void fullUpdateQuestaoWithPatch() throws Exception {
        // Initialize the database
        questaoRepository.saveAndFlush(questao);

        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();

        // Update the questao using partial update
        Questao partialUpdatedQuestao = new Questao();
        partialUpdatedQuestao.setId(questao.getId());

        partialUpdatedQuestao
            .questaoTitulo(UPDATED_QUESTAO_TITULO)
            .alternativaA(UPDATED_ALTERNATIVA_A)
            .alternativaB(UPDATED_ALTERNATIVA_B)
            .alternativaC(UPDATED_ALTERNATIVA_C)
            .alternativaD(UPDATED_ALTERNATIVA_D)
            .nivelIdade(UPDATED_NIVEL_IDADE)
            .assunto(UPDATED_ASSUNTO)
            .alternativaCerta(UPDATED_ALTERNATIVA_CERTA);

        restQuestaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestao))
            )
            .andExpect(status().isOk());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
        Questao testQuestao = questaoList.get(questaoList.size() - 1);
        assertThat(testQuestao.getQuestaoTitulo()).isEqualTo(UPDATED_QUESTAO_TITULO);
        assertThat(testQuestao.getAlternativaA()).isEqualTo(UPDATED_ALTERNATIVA_A);
        assertThat(testQuestao.getAlternativaB()).isEqualTo(UPDATED_ALTERNATIVA_B);
        assertThat(testQuestao.getAlternativaC()).isEqualTo(UPDATED_ALTERNATIVA_C);
        assertThat(testQuestao.getAlternativaD()).isEqualTo(UPDATED_ALTERNATIVA_D);
        assertThat(testQuestao.getNivelIdade()).isEqualTo(UPDATED_NIVEL_IDADE);
        assertThat(testQuestao.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testQuestao.getAlternativaCerta()).isEqualTo(UPDATED_ALTERNATIVA_CERTA);
    }

    @Test
    @Transactional
    void patchNonExistingQuestao() throws Exception {
        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();
        questao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestao() throws Exception {
        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();
        questao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestao() throws Exception {
        int databaseSizeBeforeUpdate = questaoRepository.findAll().size();
        questao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questao in the database
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestao() throws Exception {
        // Initialize the database
        questaoRepository.saveAndFlush(questao);

        int databaseSizeBeforeDelete = questaoRepository.findAll().size();

        // Delete the questao
        restQuestaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, questao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questao> questaoList = questaoRepository.findAll();
        assertThat(questaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
