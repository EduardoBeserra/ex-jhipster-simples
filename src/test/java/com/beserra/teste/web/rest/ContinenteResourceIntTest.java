package com.beserra.teste.web.rest;

import com.beserra.teste.LojaComumApp;

import com.beserra.teste.domain.Continente;
import com.beserra.teste.repository.ContinenteRepository;
import com.beserra.teste.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.beserra.teste.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContinenteResource REST controller.
 *
 * @see ContinenteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaComumApp.class)
public class ContinenteResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private ContinenteRepository continenteRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContinenteMockMvc;

    private Continente continente;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContinenteResource continenteResource = new ContinenteResource(continenteRepository);
        this.restContinenteMockMvc = MockMvcBuilders.standaloneSetup(continenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Continente createEntity(EntityManager em) {
        Continente continente = new Continente()
            .nome(DEFAULT_NOME);
        return continente;
    }

    @Before
    public void initTest() {
        continente = createEntity(em);
    }

    @Test
    @Transactional
    public void createContinente() throws Exception {
        int databaseSizeBeforeCreate = continenteRepository.findAll().size();

        // Create the Continente
        restContinenteMockMvc.perform(post("/api/continentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(continente)))
            .andExpect(status().isCreated());

        // Validate the Continente in the database
        List<Continente> continenteList = continenteRepository.findAll();
        assertThat(continenteList).hasSize(databaseSizeBeforeCreate + 1);
        Continente testContinente = continenteList.get(continenteList.size() - 1);
        assertThat(testContinente.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createContinenteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = continenteRepository.findAll().size();

        // Create the Continente with an existing ID
        continente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContinenteMockMvc.perform(post("/api/continentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(continente)))
            .andExpect(status().isBadRequest());

        // Validate the Continente in the database
        List<Continente> continenteList = continenteRepository.findAll();
        assertThat(continenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = continenteRepository.findAll().size();
        // set the field null
        continente.setNome(null);

        // Create the Continente, which fails.

        restContinenteMockMvc.perform(post("/api/continentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(continente)))
            .andExpect(status().isBadRequest());

        List<Continente> continenteList = continenteRepository.findAll();
        assertThat(continenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContinentes() throws Exception {
        // Initialize the database
        continenteRepository.saveAndFlush(continente);

        // Get all the continenteList
        restContinenteMockMvc.perform(get("/api/continentes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(continente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }
    

    @Test
    @Transactional
    public void getContinente() throws Exception {
        // Initialize the database
        continenteRepository.saveAndFlush(continente);

        // Get the continente
        restContinenteMockMvc.perform(get("/api/continentes/{id}", continente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(continente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingContinente() throws Exception {
        // Get the continente
        restContinenteMockMvc.perform(get("/api/continentes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContinente() throws Exception {
        // Initialize the database
        continenteRepository.saveAndFlush(continente);

        int databaseSizeBeforeUpdate = continenteRepository.findAll().size();

        // Update the continente
        Continente updatedContinente = continenteRepository.findById(continente.getId()).get();
        // Disconnect from session so that the updates on updatedContinente are not directly saved in db
        em.detach(updatedContinente);
        updatedContinente
            .nome(UPDATED_NOME);

        restContinenteMockMvc.perform(put("/api/continentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContinente)))
            .andExpect(status().isOk());

        // Validate the Continente in the database
        List<Continente> continenteList = continenteRepository.findAll();
        assertThat(continenteList).hasSize(databaseSizeBeforeUpdate);
        Continente testContinente = continenteList.get(continenteList.size() - 1);
        assertThat(testContinente.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingContinente() throws Exception {
        int databaseSizeBeforeUpdate = continenteRepository.findAll().size();

        // Create the Continente

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContinenteMockMvc.perform(put("/api/continentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(continente)))
            .andExpect(status().isBadRequest());

        // Validate the Continente in the database
        List<Continente> continenteList = continenteRepository.findAll();
        assertThat(continenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContinente() throws Exception {
        // Initialize the database
        continenteRepository.saveAndFlush(continente);

        int databaseSizeBeforeDelete = continenteRepository.findAll().size();

        // Get the continente
        restContinenteMockMvc.perform(delete("/api/continentes/{id}", continente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Continente> continenteList = continenteRepository.findAll();
        assertThat(continenteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Continente.class);
        Continente continente1 = new Continente();
        continente1.setId(1L);
        Continente continente2 = new Continente();
        continente2.setId(continente1.getId());
        assertThat(continente1).isEqualTo(continente2);
        continente2.setId(2L);
        assertThat(continente1).isNotEqualTo(continente2);
        continente1.setId(null);
        assertThat(continente1).isNotEqualTo(continente2);
    }
}
