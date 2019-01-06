package com.gradingapp.web.rest;

import com.gradingapp.GradingApp;

import com.gradingapp.domain.FdaiNummer;
import com.gradingapp.repository.FdaiNummerRepository;
import com.gradingapp.web.rest.errors.ExceptionTranslator;

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


import static com.gradingapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FdaiNummerResource REST controller.
 *
 * @see FdaiNummerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradingApp.class)
public class FdaiNummerResourceIntTest {

    private static final String DEFAULT_FDAINUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FDAINUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    @Autowired
    private FdaiNummerRepository fdaiNummerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFdaiNummerMockMvc;

    private FdaiNummer fdaiNummer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FdaiNummerResource fdaiNummerResource = new FdaiNummerResource(fdaiNummerRepository);
        this.restFdaiNummerMockMvc = MockMvcBuilders.standaloneSetup(fdaiNummerResource)
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
    public static FdaiNummer createEntity(EntityManager em) {
        FdaiNummer fdaiNummer = new FdaiNummer()
            .fdainumber(DEFAULT_FDAINUMBER)
            .ip(DEFAULT_IP);
        return fdaiNummer;
    }

    @Before
    public void initTest() {
        fdaiNummer = createEntity(em);
    }

    @Test
    @Transactional
    public void createFdaiNummer() throws Exception {
        int databaseSizeBeforeCreate = fdaiNummerRepository.findAll().size();

        // Create the FdaiNummer
        restFdaiNummerMockMvc.perform(post("/api/fdai-nummers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fdaiNummer)))
            .andExpect(status().isCreated());

        // Validate the FdaiNummer in the database
        List<FdaiNummer> fdaiNummerList = fdaiNummerRepository.findAll();
        assertThat(fdaiNummerList).hasSize(databaseSizeBeforeCreate + 1);
        FdaiNummer testFdaiNummer = fdaiNummerList.get(fdaiNummerList.size() - 1);
        assertThat(testFdaiNummer.getFdainumber()).isEqualTo(DEFAULT_FDAINUMBER);
        assertThat(testFdaiNummer.getIp()).isEqualTo(DEFAULT_IP);
    }

    @Test
    @Transactional
    public void createFdaiNummerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fdaiNummerRepository.findAll().size();

        // Create the FdaiNummer with an existing ID
        fdaiNummer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFdaiNummerMockMvc.perform(post("/api/fdai-nummers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fdaiNummer)))
            .andExpect(status().isBadRequest());

        // Validate the FdaiNummer in the database
        List<FdaiNummer> fdaiNummerList = fdaiNummerRepository.findAll();
        assertThat(fdaiNummerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFdaiNummers() throws Exception {
        // Initialize the database
        fdaiNummerRepository.saveAndFlush(fdaiNummer);

        // Get all the fdaiNummerList
        restFdaiNummerMockMvc.perform(get("/api/fdai-nummers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fdaiNummer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fdainumber").value(hasItem(DEFAULT_FDAINUMBER.toString())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())));
    }
    
    @Test
    @Transactional
    public void getFdaiNummer() throws Exception {
        // Initialize the database
        fdaiNummerRepository.saveAndFlush(fdaiNummer);

        // Get the fdaiNummer
        restFdaiNummerMockMvc.perform(get("/api/fdai-nummers/{id}", fdaiNummer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fdaiNummer.getId().intValue()))
            .andExpect(jsonPath("$.fdainumber").value(DEFAULT_FDAINUMBER.toString()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFdaiNummer() throws Exception {
        // Get the fdaiNummer
        restFdaiNummerMockMvc.perform(get("/api/fdai-nummers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFdaiNummer() throws Exception {
        // Initialize the database
        fdaiNummerRepository.saveAndFlush(fdaiNummer);

        int databaseSizeBeforeUpdate = fdaiNummerRepository.findAll().size();

        // Update the fdaiNummer
        FdaiNummer updatedFdaiNummer = fdaiNummerRepository.findById(fdaiNummer.getId()).get();
        // Disconnect from session so that the updates on updatedFdaiNummer are not directly saved in db
        em.detach(updatedFdaiNummer);
        updatedFdaiNummer
            .fdainumber(UPDATED_FDAINUMBER)
            .ip(UPDATED_IP);

        restFdaiNummerMockMvc.perform(put("/api/fdai-nummers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFdaiNummer)))
            .andExpect(status().isOk());

        // Validate the FdaiNummer in the database
        List<FdaiNummer> fdaiNummerList = fdaiNummerRepository.findAll();
        assertThat(fdaiNummerList).hasSize(databaseSizeBeforeUpdate);
        FdaiNummer testFdaiNummer = fdaiNummerList.get(fdaiNummerList.size() - 1);
        assertThat(testFdaiNummer.getFdainumber()).isEqualTo(UPDATED_FDAINUMBER);
        assertThat(testFdaiNummer.getIp()).isEqualTo(UPDATED_IP);
    }

    @Test
    @Transactional
    public void updateNonExistingFdaiNummer() throws Exception {
        int databaseSizeBeforeUpdate = fdaiNummerRepository.findAll().size();

        // Create the FdaiNummer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFdaiNummerMockMvc.perform(put("/api/fdai-nummers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fdaiNummer)))
            .andExpect(status().isBadRequest());

        // Validate the FdaiNummer in the database
        List<FdaiNummer> fdaiNummerList = fdaiNummerRepository.findAll();
        assertThat(fdaiNummerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFdaiNummer() throws Exception {
        // Initialize the database
        fdaiNummerRepository.saveAndFlush(fdaiNummer);

        int databaseSizeBeforeDelete = fdaiNummerRepository.findAll().size();

        // Get the fdaiNummer
        restFdaiNummerMockMvc.perform(delete("/api/fdai-nummers/{id}", fdaiNummer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FdaiNummer> fdaiNummerList = fdaiNummerRepository.findAll();
        assertThat(fdaiNummerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FdaiNummer.class);
        FdaiNummer fdaiNummer1 = new FdaiNummer();
        fdaiNummer1.setId(1L);
        FdaiNummer fdaiNummer2 = new FdaiNummer();
        fdaiNummer2.setId(fdaiNummer1.getId());
        assertThat(fdaiNummer1).isEqualTo(fdaiNummer2);
        fdaiNummer2.setId(2L);
        assertThat(fdaiNummer1).isNotEqualTo(fdaiNummer2);
        fdaiNummer1.setId(null);
        assertThat(fdaiNummer1).isNotEqualTo(fdaiNummer2);
    }
}
