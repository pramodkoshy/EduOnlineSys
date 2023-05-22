package education.web.rest;


import education.domain.Exam;
import education.repository.ExamRepository;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionOperations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.sql.Connection;
import java.util.List;

import education.service.dto.ExamDTO;
import education.service.mapper.ExamMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link ExamResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private ExamMapper examMapper;
    @Inject
    private ExamRepository examRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private Exam exam;

    @BeforeEach
    public void initTest() {
        exam = createEntity(transactionManager, em);
    }

    @AfterEach
    public void cleanUpTest() {
        deleteAll(transactionManager, em);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exam createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Exam exam = new Exam()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return exam;
    }

    /**
     * Delete all exam entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Exam.class);
    }


    @Test
    public void createExam() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();

        ExamDTO examDTO = examMapper.toDto(exam);

        // Create the Exam
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.POST("/api/exams", examDTO), ExamDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate + 1);
        Exam testExam = examList.get(examList.size() - 1);

        assertThat(testExam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void createExamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // Create the Exam with an existing ID
        exam.setId(1L);
        ExamDTO examDTO = examMapper.toDto(exam);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.POST("/api/exams", examDTO), ExamDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setName(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        @SuppressWarnings("unchecked")
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.POST("/api/exams", examDTO), ExamDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setDescription(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        @SuppressWarnings("unchecked")
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.POST("/api/exams", examDTO), ExamDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllExams() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get the examList w/ all the exams
        List<ExamDTO> exams = client.retrieve(HttpRequest.GET("/api/exams?eagerload=true"), Argument.listOf(ExamDTO.class)).blockingFirst();
        ExamDTO testExam = exams.get(0);


        assertThat(testExam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void getExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get the exam
        ExamDTO testExam = client.retrieve(HttpRequest.GET("/api/exams/" + exam.getId()), ExamDTO.class).blockingFirst();


        assertThat(testExam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void getNonExistingExam() throws Exception {
        // Get the exam
        @SuppressWarnings("unchecked")
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.GET("/api/exams/"+ Long.MAX_VALUE), ExamDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam
        Exam updatedExam = examRepository.findById(exam.getId()).get();

        updatedExam
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        ExamDTO updatedExamDTO = examMapper.toDto(updatedExam);

        @SuppressWarnings("unchecked")
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.PUT("/api/exams", updatedExamDTO), ExamDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        Exam testExam = examList.get(examList.size() - 1);

        assertThat(testExam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    public void updateNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.PUT("/api/exams", examDTO), ExamDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteExam() throws Exception {
        // Initialize the database with one entity
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeDelete = examRepository.findAll().size();

        // Delete the exam
        @SuppressWarnings("unchecked")
        HttpResponse<ExamDTO> response = client.exchange(HttpRequest.DELETE("/api/exams/"+ exam.getId()), ExamDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exam.class);
        Exam exam1 = new Exam();
        exam1.setId(1L);
        Exam exam2 = new Exam();
        exam2.setId(exam1.getId());
        assertThat(exam1).isEqualTo(exam2);
        exam2.setId(2L);
        assertThat(exam1).isNotEqualTo(exam2);
        exam1.setId(null);
        assertThat(exam1).isNotEqualTo(exam2);
    }
}
