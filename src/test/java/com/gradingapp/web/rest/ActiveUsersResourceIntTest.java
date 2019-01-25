package com.gradingapp.web.rest;

import com.gradingapp.GradingApp;

import com.gradingapp.domain.ActiveUsers;
import com.gradingapp.repository.ActiveUsersRepository;
import com.gradingapp.repository.FdaiNummerRepository;
import com.gradingapp.service.ActiveUsersService;
import com.gradingapp.service.UserService;
import com.gradingapp.service.dto.ActiveUsersDTO;
import com.gradingapp.service.mapper.ActiveUsersMapper;
import com.gradingapp.web.rest.errors.ExceptionTranslator;
import com.gradingapp.service.dto.ActiveUsersCriteria;
import com.gradingapp.service.ActiveUsersQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.gradingapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ActiveUsersResource REST controller.
 *
 * @see ActiveUsersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradingApp.class)
public class ActiveUsersResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_LOGIN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOGIN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LOGOUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOGOUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ActiveUsersRepository activeUsersRepository;

    @Autowired
    private ActiveUsersMapper activeUsersMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private FdaiNummerRepository fdaiNummerRepository;

    @Autowired
    private ActiveUsersService activeUsersService;

    @Autowired
    private ActiveUsersQueryService activeUsersQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActiveUsersMockMvc;

    private ActiveUsers activeUsers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActiveUsersResource activeUsersResource = new ActiveUsersResource(activeUsersService, activeUsersQueryService, userService, fdaiNummerRepository);
        this.restActiveUsersMockMvc = MockMvcBuilders.standaloneSetup(activeUsersResource)
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
    public static ActiveUsers createEntity(EntityManager em) {
        ActiveUsers activeUsers = new ActiveUsers()
            .username(DEFAULT_USERNAME)
            .login_time(DEFAULT_LOGIN_TIME)
            .logout_time(DEFAULT_LOGOUT_TIME)
            .active(DEFAULT_ACTIVE);
        return activeUsers;
    }

    @Before
    public void initTest() {
        activeUsers = createEntity(em);
    }

    @Test
    @Transactional
    public void createActiveUsers() throws Exception {
        int databaseSizeBeforeCreate = activeUsersRepository.findAll().size();

        // Create the ActiveUsers
        ActiveUsersDTO activeUsersDTO = activeUsersMapper.toDto(activeUsers);
        restActiveUsersMockMvc.perform(post("/api/active-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activeUsersDTO)))
            .andExpect(status().isCreated());

        // Validate the ActiveUsers in the database
        List<ActiveUsers> activeUsersList = activeUsersRepository.findAll();
        assertThat(activeUsersList).hasSize(databaseSizeBeforeCreate + 1);
        ActiveUsers testActiveUsers = activeUsersList.get(activeUsersList.size() - 1);
        assertThat(testActiveUsers.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testActiveUsers.getLogin_time()).isEqualTo(DEFAULT_LOGIN_TIME);
        assertThat(testActiveUsers.getLogout_time()).isEqualTo(DEFAULT_LOGOUT_TIME);
        assertThat(testActiveUsers.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createActiveUsersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activeUsersRepository.findAll().size();

        // Create the ActiveUsers with an existing ID
        activeUsers.setId(1L);
        ActiveUsersDTO activeUsersDTO = activeUsersMapper.toDto(activeUsers);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiveUsersMockMvc.perform(post("/api/active-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activeUsersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActiveUsers in the database
        List<ActiveUsers> activeUsersList = activeUsersRepository.findAll();
        assertThat(activeUsersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActiveUsers() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList
        restActiveUsersMockMvc.perform(get("/api/active-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activeUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].login_time").value(hasItem(DEFAULT_LOGIN_TIME.toString())))
            .andExpect(jsonPath("$.[*].logout_time").value(hasItem(DEFAULT_LOGOUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getActiveUsers() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get the activeUsers
        restActiveUsersMockMvc.perform(get("/api/active-users/{id}", activeUsers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activeUsers.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.login_time").value(DEFAULT_LOGIN_TIME.toString()))
            .andExpect(jsonPath("$.logout_time").value(DEFAULT_LOGOUT_TIME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllActiveUsersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where username equals to DEFAULT_USERNAME
        defaultActiveUsersShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the activeUsersList where username equals to UPDATED_USERNAME
        defaultActiveUsersShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultActiveUsersShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the activeUsersList where username equals to UPDATED_USERNAME
        defaultActiveUsersShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where username is not null
        defaultActiveUsersShouldBeFound("username.specified=true");

        // Get all the activeUsersList where username is null
        defaultActiveUsersShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    public void getAllActiveUsersByLogin_timeIsEqualToSomething() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where login_time equals to DEFAULT_LOGIN_TIME
        defaultActiveUsersShouldBeFound("login_time.equals=" + DEFAULT_LOGIN_TIME);

        // Get all the activeUsersList where login_time equals to UPDATED_LOGIN_TIME
        defaultActiveUsersShouldNotBeFound("login_time.equals=" + UPDATED_LOGIN_TIME);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByLogin_timeIsInShouldWork() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where login_time in DEFAULT_LOGIN_TIME or UPDATED_LOGIN_TIME
        defaultActiveUsersShouldBeFound("login_time.in=" + DEFAULT_LOGIN_TIME + "," + UPDATED_LOGIN_TIME);

        // Get all the activeUsersList where login_time equals to UPDATED_LOGIN_TIME
        defaultActiveUsersShouldNotBeFound("login_time.in=" + UPDATED_LOGIN_TIME);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByLogin_timeIsNullOrNotNull() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where login_time is not null
        defaultActiveUsersShouldBeFound("login_time.specified=true");

        // Get all the activeUsersList where login_time is null
        defaultActiveUsersShouldNotBeFound("login_time.specified=false");
    }

    @Test
    @Transactional
    public void getAllActiveUsersByLogout_timeIsEqualToSomething() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where logout_time equals to DEFAULT_LOGOUT_TIME
        defaultActiveUsersShouldBeFound("logout_time.equals=" + DEFAULT_LOGOUT_TIME);

        // Get all the activeUsersList where logout_time equals to UPDATED_LOGOUT_TIME
        defaultActiveUsersShouldNotBeFound("logout_time.equals=" + UPDATED_LOGOUT_TIME);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByLogout_timeIsInShouldWork() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where logout_time in DEFAULT_LOGOUT_TIME or UPDATED_LOGOUT_TIME
        defaultActiveUsersShouldBeFound("logout_time.in=" + DEFAULT_LOGOUT_TIME + "," + UPDATED_LOGOUT_TIME);

        // Get all the activeUsersList where logout_time equals to UPDATED_LOGOUT_TIME
        defaultActiveUsersShouldNotBeFound("logout_time.in=" + UPDATED_LOGOUT_TIME);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByLogout_timeIsNullOrNotNull() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where logout_time is not null
        defaultActiveUsersShouldBeFound("logout_time.specified=true");

        // Get all the activeUsersList where logout_time is null
        defaultActiveUsersShouldNotBeFound("logout_time.specified=false");
    }

    @Test
    @Transactional
    public void getAllActiveUsersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where active equals to DEFAULT_ACTIVE
        defaultActiveUsersShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the activeUsersList where active equals to UPDATED_ACTIVE
        defaultActiveUsersShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultActiveUsersShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the activeUsersList where active equals to UPDATED_ACTIVE
        defaultActiveUsersShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllActiveUsersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        // Get all the activeUsersList where active is not null
        defaultActiveUsersShouldBeFound("active.specified=true");

        // Get all the activeUsersList where active is null
        defaultActiveUsersShouldNotBeFound("active.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultActiveUsersShouldBeFound(String filter) throws Exception {
        restActiveUsersMockMvc.perform(get("/api/active-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activeUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].login_time").value(hasItem(DEFAULT_LOGIN_TIME.toString())))
            .andExpect(jsonPath("$.[*].logout_time").value(hasItem(DEFAULT_LOGOUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restActiveUsersMockMvc.perform(get("/api/active-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultActiveUsersShouldNotBeFound(String filter) throws Exception {
        restActiveUsersMockMvc.perform(get("/api/active-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActiveUsersMockMvc.perform(get("/api/active-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingActiveUsers() throws Exception {
        // Get the activeUsers
        restActiveUsersMockMvc.perform(get("/api/active-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActiveUsers() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        int databaseSizeBeforeUpdate = activeUsersRepository.findAll().size();

        // Update the activeUsers
        ActiveUsers updatedActiveUsers = activeUsersRepository.findById(activeUsers.getId()).get();
        // Disconnect from session so that the updates on updatedActiveUsers are not directly saved in db
        em.detach(updatedActiveUsers);
        updatedActiveUsers
            .username(UPDATED_USERNAME)
            .login_time(UPDATED_LOGIN_TIME)
            .logout_time(UPDATED_LOGOUT_TIME)
            .active(UPDATED_ACTIVE);
        ActiveUsersDTO activeUsersDTO = activeUsersMapper.toDto(updatedActiveUsers);

        restActiveUsersMockMvc.perform(put("/api/active-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activeUsersDTO)))
            .andExpect(status().isOk());

        // Validate the ActiveUsers in the database
        List<ActiveUsers> activeUsersList = activeUsersRepository.findAll();
        assertThat(activeUsersList).hasSize(databaseSizeBeforeUpdate);
        ActiveUsers testActiveUsers = activeUsersList.get(activeUsersList.size() - 1);
        assertThat(testActiveUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testActiveUsers.getLogin_time()).isEqualTo(UPDATED_LOGIN_TIME);
        assertThat(testActiveUsers.getLogout_time()).isEqualTo(UPDATED_LOGOUT_TIME);
        assertThat(testActiveUsers.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingActiveUsers() throws Exception {
        int databaseSizeBeforeUpdate = activeUsersRepository.findAll().size();

        // Create the ActiveUsers
        ActiveUsersDTO activeUsersDTO = activeUsersMapper.toDto(activeUsers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiveUsersMockMvc.perform(put("/api/active-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activeUsersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActiveUsers in the database
        List<ActiveUsers> activeUsersList = activeUsersRepository.findAll();
        assertThat(activeUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActiveUsers() throws Exception {
        // Initialize the database
        activeUsersRepository.saveAndFlush(activeUsers);

        int databaseSizeBeforeDelete = activeUsersRepository.findAll().size();

        // Get the activeUsers
        restActiveUsersMockMvc.perform(delete("/api/active-users/{id}", activeUsers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActiveUsers> activeUsersList = activeUsersRepository.findAll();
        assertThat(activeUsersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActiveUsers.class);
        ActiveUsers activeUsers1 = new ActiveUsers();
        activeUsers1.setId(1L);
        ActiveUsers activeUsers2 = new ActiveUsers();
        activeUsers2.setId(activeUsers1.getId());
        assertThat(activeUsers1).isEqualTo(activeUsers2);
        activeUsers2.setId(2L);
        assertThat(activeUsers1).isNotEqualTo(activeUsers2);
        activeUsers1.setId(null);
        assertThat(activeUsers1).isNotEqualTo(activeUsers2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActiveUsersDTO.class);
        ActiveUsersDTO activeUsersDTO1 = new ActiveUsersDTO();
        activeUsersDTO1.setId(1L);
        ActiveUsersDTO activeUsersDTO2 = new ActiveUsersDTO();
        assertThat(activeUsersDTO1).isNotEqualTo(activeUsersDTO2);
        activeUsersDTO2.setId(activeUsersDTO1.getId());
        assertThat(activeUsersDTO1).isEqualTo(activeUsersDTO2);
        activeUsersDTO2.setId(2L);
        assertThat(activeUsersDTO1).isNotEqualTo(activeUsersDTO2);
        activeUsersDTO1.setId(null);
        assertThat(activeUsersDTO1).isNotEqualTo(activeUsersDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activeUsersMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activeUsersMapper.fromId(null)).isNull();
    }
}
