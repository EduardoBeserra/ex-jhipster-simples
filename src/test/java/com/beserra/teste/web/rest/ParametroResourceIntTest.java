package com.beserra.teste.web.rest;

import com.beserra.teste.LojaComumApp;

import com.beserra.teste.domain.Parametro;
import com.beserra.teste.repository.ParametroRepository;
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
 * Test class for the ParametroResource REST controller.
 *
 * @see ParametroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaComumApp.class)
public class ParametroResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO = "BBBBBBBBBB";

    @Autowired
    private ParametroRepository parametroRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParametroMockMvc;

    private Parametro parametro;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParametroResource parametroResource = new ParametroResource(parametroRepository);
        this.restParametroMockMvc = MockMvcBuilders.standaloneSetup(parametroResource)
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
    public static Parametro createEntity(EntityManager em) {
        Parametro parametro = new Parametro()
            .nome(DEFAULT_NOME)
            .conteudo(DEFAULT_CONTEUDO);
        return parametro;
    }

    @Before
    public void initTest() {
        parametro = createEntity(em);
    }

    @Test
    @Transactional
    public void createParametro() throws Exception {
        int databaseSizeBeforeCreate = parametroRepository.findAll().size();

        // Create the Parametro
        restParametroMockMvc.perform(post("/api/parametros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parametro)))
            .andExpect(status().isCreated());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeCreate + 1);
        Parametro testParametro = parametroList.get(parametroList.size() - 1);
        assertThat(testParametro.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testParametro.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
    }

    @Test
    @Transactional
    public void createParametroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parametroRepository.findAll().size();

        // Create the Parametro with an existing ID
        parametro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametroMockMvc.perform(post("/api/parametros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parametro)))
            .andExpect(status().isBadRequest());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroRepository.findAll().size();
        // set the field null
        parametro.setNome(null);

        // Create the Parametro, which fails.

        restParametroMockMvc.perform(post("/api/parametros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parametro)))
            .andExpect(status().isBadRequest());

        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParametros() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get all the parametroList
        restParametroMockMvc.perform(get("/api/parametros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())));
    }
    

    @Test
    @Transactional
    public void getParametro() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        // Get the parametro
        restParametroMockMvc.perform(get("/api/parametros/{id}", parametro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parametro.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingParametro() throws Exception {
        // Get the parametro
        restParametroMockMvc.perform(get("/api/parametros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParametro() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();

        // Update the parametro
        Parametro updatedParametro = parametroRepository.findById(parametro.getId()).get();
        // Disconnect from session so that the updates on updatedParametro are not directly saved in db
        em.detach(updatedParametro);
        updatedParametro
            .nome(UPDATED_NOME)
            .conteudo(UPDATED_CONTEUDO);

        restParametroMockMvc.perform(put("/api/parametros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParametro)))
            .andExpect(status().isOk());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
        Parametro testParametro = parametroList.get(parametroList.size() - 1);
        assertThat(testParametro.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testParametro.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    public void updateNonExistingParametro() throws Exception {
        int databaseSizeBeforeUpdate = parametroRepository.findAll().size();

        // Create the Parametro

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParametroMockMvc.perform(put("/api/parametros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parametro)))
            .andExpect(status().isBadRequest());

        // Validate the Parametro in the database
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParametro() throws Exception {
        // Initialize the database
        parametroRepository.saveAndFlush(parametro);

        int databaseSizeBeforeDelete = parametroRepository.findAll().size();

        // Get the parametro
        restParametroMockMvc.perform(delete("/api/parametros/{id}", parametro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Parametro> parametroList = parametroRepository.findAll();
        assertThat(parametroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parametro.class);
        Parametro parametro1 = new Parametro();
        parametro1.setId(1L);
        Parametro parametro2 = new Parametro();
        parametro2.setId(parametro1.getId());
        assertThat(parametro1).isEqualTo(parametro2);
        parametro2.setId(2L);
        assertThat(parametro1).isNotEqualTo(parametro2);
        parametro1.setId(null);
        assertThat(parametro1).isNotEqualTo(parametro2);
    }
}
