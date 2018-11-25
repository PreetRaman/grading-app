package com.gradingapp.web.rest;

import com.gradingapp.GradingApp;

import com.gradingapp.domain.Submissions;
import com.gradingapp.repository.SubmissionsRepository;
import com.gradingapp.repository.UserRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static com.gradingapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gradingapp.domain.enumeration.Course;
import com.gradingapp.domain.enumeration.Subject;
import com.gradingapp.domain.enumeration.Exercise;
/**
 * Test class for the SubmissionsResource REST controller.
 *
 * @see SubmissionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradingApp.class)
public class SubmissionsResourceIntTest {

    private static final String DEFAULT_FDAI_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FDAI_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Course DEFAULT_COURSE = Course.GSD;
    private static final Course UPDATED_COURSE = Course.AI;

    private static final Subject DEFAULT_SUBJECT = Subject.JAVA;
    private static final Subject UPDATED_SUBJECT = Subject.Mlearning;

    private static final Exercise DEFAULT_EXERCISES = Exercise.E1;
    private static final Exercise UPDATED_EXERCISES = Exercise.E2;

    private static final byte[] DEFAULT_FILES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILES_CONTENT_TYPE = "image/png";

    @Autowired
    private SubmissionsRepository submissionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubmissionsMockMvc;

    private Submissions submissions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubmissionsResource submissionsResource = new SubmissionsResource(submissionsRepository, userRepository);
        this.restSubmissionsMockMvc = MockMvcBuilders.standaloneSetup(submissionsResource)
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
    public static Submissions createEntity(EntityManager em) {
        Submissions submissions = new Submissions()
            .fdaiNumber(DEFAULT_FDAI_NUMBER)
            .name(DEFAULT_NAME)
            .course(DEFAULT_COURSE)
            .subject(DEFAULT_SUBJECT)
            .exercises(DEFAULT_EXERCISES)
            .files(DEFAULT_FILES)
            .filesContentType(DEFAULT_FILES_CONTENT_TYPE);
        return submissions;
    }

    @Before
    public void initTest() {
        submissions = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubmissions() throws Exception {
        int databaseSizeBeforeCreate = submissionsRepository.findAll().size();

        // Create the Submissions
        restSubmissionsMockMvc.perform(post("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submissions)))
            .andExpect(status().isCreated());

        // Validate the Submissions in the database
        List<Submissions> submissionsList = submissionsRepository.findAll();
        assertThat(submissionsList).hasSize(databaseSizeBeforeCreate + 1);
        Submissions testSubmissions = submissionsList.get(submissionsList.size() - 1);
        assertThat(testSubmissions.getFdaiNumber()).isEqualTo(DEFAULT_FDAI_NUMBER);
        assertThat(testSubmissions.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubmissions.getCourse()).isEqualTo(DEFAULT_COURSE);
        assertThat(testSubmissions.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testSubmissions.getExercises()).isEqualTo(DEFAULT_EXERCISES);
        assertThat(testSubmissions.getFiles()).isEqualTo(DEFAULT_FILES);
        assertThat(testSubmissions.getFilesContentType()).isEqualTo(DEFAULT_FILES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSubmissionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = submissionsRepository.findAll().size();

        // Create the Submissions with an existing ID
        submissions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionsMockMvc.perform(post("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submissions)))
            .andExpect(status().isBadRequest());

        // Validate the Submissions in the database
        List<Submissions> submissionsList = submissionsRepository.findAll();
        assertThat(submissionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSubmissions() throws Exception {
        // Initialize the database
        submissionsRepository.saveAndFlush(submissions);

        // Get all the submissionsList
        restSubmissionsMockMvc.perform(get("/api/submissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].fdaiNumber").value(hasItem(DEFAULT_FDAI_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].course").value(hasItem(DEFAULT_COURSE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].exercises").value(hasItem(DEFAULT_EXERCISES.toString())))
            .andExpect(jsonPath("$.[*].filesContentType").value(hasItem(DEFAULT_FILES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].files").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILES))));
    }

    @Test
    @Transactional
    public void getSubmissions() throws Exception {
        // Initialize the database
        submissionsRepository.saveAndFlush(submissions);

        // Get the submissions
        restSubmissionsMockMvc.perform(get("/api/submissions/{id}", submissions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(submissions.getId().intValue()))
            .andExpect(jsonPath("$.fdaiNumber").value(DEFAULT_FDAI_NUMBER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.course").value(DEFAULT_COURSE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.exercises").value(DEFAULT_EXERCISES.toString()))
            .andExpect(jsonPath("$.filesContentType").value(DEFAULT_FILES_CONTENT_TYPE))
            .andExpect(jsonPath("$.files").value(Base64Utils.encodeToString(DEFAULT_FILES)));
    }

    @Test
    @Transactional
    public void getNonExistingSubmissions() throws Exception {
        // Get the submissions
        restSubmissionsMockMvc.perform(get("/api/submissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubmissions() throws Exception {
        // Initialize the database
        submissionsRepository.saveAndFlush(submissions);

        int databaseSizeBeforeUpdate = submissionsRepository.findAll().size();

        // Update the submissions
        Submissions updatedSubmissions = submissionsRepository.findById(submissions.getId()).get();
        // Disconnect from session so that the updates on updatedSubmissions are not directly saved in db
        em.detach(updatedSubmissions);
        updatedSubmissions
            .fdaiNumber(UPDATED_FDAI_NUMBER)
            .name(UPDATED_NAME)
            .course(UPDATED_COURSE)
            .subject(UPDATED_SUBJECT)
            .exercises(UPDATED_EXERCISES)
            .files(UPDATED_FILES)
            .filesContentType(UPDATED_FILES_CONTENT_TYPE);

        restSubmissionsMockMvc.perform(put("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubmissions)))
            .andExpect(status().isOk());

        // Validate the Submissions in the database
        List<Submissions> submissionsList = submissionsRepository.findAll();
        assertThat(submissionsList).hasSize(databaseSizeBeforeUpdate);
        Submissions testSubmissions = submissionsList.get(submissionsList.size() - 1);
        assertThat(testSubmissions.getFdaiNumber()).isEqualTo(UPDATED_FDAI_NUMBER);
        assertThat(testSubmissions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubmissions.getCourse()).isEqualTo(UPDATED_COURSE);
        assertThat(testSubmissions.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testSubmissions.getExercises()).isEqualTo(UPDATED_EXERCISES);
        assertThat(testSubmissions.getFiles()).isEqualTo(UPDATED_FILES);
        assertThat(testSubmissions.getFilesContentType()).isEqualTo(UPDATED_FILES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSubmissions() throws Exception {
        int databaseSizeBeforeUpdate = submissionsRepository.findAll().size();

        // Create the Submissions

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionsMockMvc.perform(put("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submissions)))
            .andExpect(status().isBadRequest());

        // Validate the Submissions in the database
        List<Submissions> submissionsList = submissionsRepository.findAll();
        assertThat(submissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubmissions() throws Exception {
        // Initialize the database
        submissionsRepository.saveAndFlush(submissions);

        int databaseSizeBeforeDelete = submissionsRepository.findAll().size();

        // Get the submissions
        restSubmissionsMockMvc.perform(delete("/api/submissions/{id}", submissions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Submissions> submissionsList = submissionsRepository.findAll();
        assertThat(submissionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Submissions.class);
        Submissions submissions1 = new Submissions();
        submissions1.setId(1L);
        Submissions submissions2 = new Submissions();
        submissions2.setId(submissions1.getId());
        assertThat(submissions1).isEqualTo(submissions2);
        submissions2.setId(2L);
        assertThat(submissions1).isNotEqualTo(submissions2);
        submissions1.setId(null);
        assertThat(submissions1).isNotEqualTo(submissions2);
    }
}
