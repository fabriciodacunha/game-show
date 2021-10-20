package com.padrinho.gameshow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.padrinho.gameshow.IntegrationTest;
import com.padrinho.gameshow.domain.Participante;
import com.padrinho.gameshow.repository.ParticipanteRepository;
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
 * Integration tests for the {@link ParticipanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParticipanteResourceIT {

    private static final String DEFAULT_CARASTRO_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_CARASTRO_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICIPANTE_NOME = "AAAAAAAAAA";
    private static final String UPDATED_PARTICIPANTE_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICIPANTE_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PARTICIPANTE_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PARTICIPANTE_DATA_DE_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/participantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParticipanteMockMvc;

    private Participante participante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participante createEntity(EntityManager em) {
        Participante participante = new Participante()
            .carastroUsuario(DEFAULT_CARASTRO_USUARIO)
            .participanteNome(DEFAULT_PARTICIPANTE_NOME)
            .participanteEmail(DEFAULT_PARTICIPANTE_EMAIL)
            .participanteDataDeNascimento(DEFAULT_PARTICIPANTE_DATA_DE_NASCIMENTO);
        return participante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participante createUpdatedEntity(EntityManager em) {
        Participante participante = new Participante()
            .carastroUsuario(UPDATED_CARASTRO_USUARIO)
            .participanteNome(UPDATED_PARTICIPANTE_NOME)
            .participanteEmail(UPDATED_PARTICIPANTE_EMAIL)
            .participanteDataDeNascimento(UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO);
        return participante;
    }

    @BeforeEach
    public void initTest() {
        participante = createEntity(em);
    }

    @Test
    @Transactional
    void createParticipante() throws Exception {
        int databaseSizeBeforeCreate = participanteRepository.findAll().size();
        // Create the Participante
        restParticipanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participante)))
            .andExpect(status().isCreated());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeCreate + 1);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getCarastroUsuario()).isEqualTo(DEFAULT_CARASTRO_USUARIO);
        assertThat(testParticipante.getParticipanteNome()).isEqualTo(DEFAULT_PARTICIPANTE_NOME);
        assertThat(testParticipante.getParticipanteEmail()).isEqualTo(DEFAULT_PARTICIPANTE_EMAIL);
        assertThat(testParticipante.getParticipanteDataDeNascimento()).isEqualTo(DEFAULT_PARTICIPANTE_DATA_DE_NASCIMENTO);
    }

    @Test
    @Transactional
    void createParticipanteWithExistingId() throws Exception {
        // Create the Participante with an existing ID
        participante.setId(1L);

        int databaseSizeBeforeCreate = participanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participante)))
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParticipantes() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participante.getId().intValue())))
            .andExpect(jsonPath("$.[*].carastroUsuario").value(hasItem(DEFAULT_CARASTRO_USUARIO)))
            .andExpect(jsonPath("$.[*].participanteNome").value(hasItem(DEFAULT_PARTICIPANTE_NOME)))
            .andExpect(jsonPath("$.[*].participanteEmail").value(hasItem(DEFAULT_PARTICIPANTE_EMAIL)))
            .andExpect(jsonPath("$.[*].participanteDataDeNascimento").value(hasItem(DEFAULT_PARTICIPANTE_DATA_DE_NASCIMENTO.toString())));
    }

    @Test
    @Transactional
    void getParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get the participante
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL_ID, participante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(participante.getId().intValue()))
            .andExpect(jsonPath("$.carastroUsuario").value(DEFAULT_CARASTRO_USUARIO))
            .andExpect(jsonPath("$.participanteNome").value(DEFAULT_PARTICIPANTE_NOME))
            .andExpect(jsonPath("$.participanteEmail").value(DEFAULT_PARTICIPANTE_EMAIL))
            .andExpect(jsonPath("$.participanteDataDeNascimento").value(DEFAULT_PARTICIPANTE_DATA_DE_NASCIMENTO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingParticipante() throws Exception {
        // Get the participante
        restParticipanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante
        Participante updatedParticipante = participanteRepository.findById(participante.getId()).get();
        // Disconnect from session so that the updates on updatedParticipante are not directly saved in db
        em.detach(updatedParticipante);
        updatedParticipante
            .carastroUsuario(UPDATED_CARASTRO_USUARIO)
            .participanteNome(UPDATED_PARTICIPANTE_NOME)
            .participanteEmail(UPDATED_PARTICIPANTE_EMAIL)
            .participanteDataDeNascimento(UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO);

        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParticipante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParticipante))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getCarastroUsuario()).isEqualTo(UPDATED_CARASTRO_USUARIO);
        assertThat(testParticipante.getParticipanteNome()).isEqualTo(UPDATED_PARTICIPANTE_NOME);
        assertThat(testParticipante.getParticipanteEmail()).isEqualTo(UPDATED_PARTICIPANTE_EMAIL);
        assertThat(testParticipante.getParticipanteDataDeNascimento()).isEqualTo(UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO);
    }

    @Test
    @Transactional
    void putNonExistingParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, participante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParticipanteWithPatch() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante using partial update
        Participante partialUpdatedParticipante = new Participante();
        partialUpdatedParticipante.setId(participante.getId());

        partialUpdatedParticipante.participanteDataDeNascimento(UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO);

        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipante))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getCarastroUsuario()).isEqualTo(DEFAULT_CARASTRO_USUARIO);
        assertThat(testParticipante.getParticipanteNome()).isEqualTo(DEFAULT_PARTICIPANTE_NOME);
        assertThat(testParticipante.getParticipanteEmail()).isEqualTo(DEFAULT_PARTICIPANTE_EMAIL);
        assertThat(testParticipante.getParticipanteDataDeNascimento()).isEqualTo(UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO);
    }

    @Test
    @Transactional
    void fullUpdateParticipanteWithPatch() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante using partial update
        Participante partialUpdatedParticipante = new Participante();
        partialUpdatedParticipante.setId(participante.getId());

        partialUpdatedParticipante
            .carastroUsuario(UPDATED_CARASTRO_USUARIO)
            .participanteNome(UPDATED_PARTICIPANTE_NOME)
            .participanteEmail(UPDATED_PARTICIPANTE_EMAIL)
            .participanteDataDeNascimento(UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO);

        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipante))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getCarastroUsuario()).isEqualTo(UPDATED_CARASTRO_USUARIO);
        assertThat(testParticipante.getParticipanteNome()).isEqualTo(UPDATED_PARTICIPANTE_NOME);
        assertThat(testParticipante.getParticipanteEmail()).isEqualTo(UPDATED_PARTICIPANTE_EMAIL);
        assertThat(testParticipante.getParticipanteDataDeNascimento()).isEqualTo(UPDATED_PARTICIPANTE_DATA_DE_NASCIMENTO);
    }

    @Test
    @Transactional
    void patchNonExistingParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, participante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(participante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeDelete = participanteRepository.findAll().size();

        // Delete the participante
        restParticipanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, participante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
