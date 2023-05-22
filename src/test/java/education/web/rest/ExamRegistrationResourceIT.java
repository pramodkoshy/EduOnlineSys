package education.web.rest;


import education.domain.ExamRegistration;
import education.repository.ExamRegistrationRepository;

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

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Connection;
import java.util.List;

import education.service.dto.ExamRegistrationDTO;
import education.service.mapper.ExamRegistrationMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link ExamRegistrationResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamRegistrationResourceIT {

    private static final LocalDate DEFAULT_ENROLMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENROLMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ExamRegistrationMapper examRegistrationMapper;
    @Inject
    private ExamRegistrationRepository examRegistrationRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private ExamRegistration examRegistration;

    @BeforeEach
    public void initTest() {
        examRegistration = createEntity(transactionManager, em);
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
    public static ExamRegistration createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        ExamRegistration examRegistration = new ExamRegistration()
            .enrolmentDate(DEFAULT_ENROLMENT_DATE);
        return examRegistration;
    }

    /**
     * Delete all examRegistration entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, ExamRegistration.class);
    }


    @Test
    public void createExamRegistration() throws Exception {
        int databaseSizeBeforeCreate = examRegistrationRepository.findAll().size();

        ExamRegistrationDTO examRegistrationDTO = examRegistrationMapper.toDto(examRegistration);

        // Create the ExamRegistration
        HttpResponse<ExamRegistrationDTO> response = client.exchange(HttpRequest.POST("/api/exam-registrations", examRegistrationDTO), ExamRegistrationDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the ExamRegistration in the database
        List<ExamRegistration> examRegistrationList = examRegistrationRepository.findAll();
        assertThat(examRegistrationList).hasSize(databaseSizeBeforeCreate + 1);
        ExamRegistration testExamRegistration = examRegistrationList.get(examRegistrationList.size() - 1);

        assertThat(testExamRegistration.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void createExamRegistrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examRegistrationRepository.findAll().size();

        // Create the ExamRegistration with an existing ID
        examRegistration.setId(1L);
        ExamRegistrationDTO examRegistrationDTO = examRegistrationMapper.toDto(examRegistration);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<ExamRegistrationDTO> response = client.exchange(HttpRequest.POST("/api/exam-registrations", examRegistrationDTO), ExamRegistrationDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamRegistrationDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the ExamRegistration in the database
        List<ExamRegistration> examRegistrationList = examRegistrationRepository.findAll();
        assertThat(examRegistrationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkEnrolmentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRegistrationRepository.findAll().size();
        // set the field null
        examRegistration.setEnrolmentDate(null);

        // Create the ExamRegistration, which fails.
        ExamRegistrationDTO examRegistrationDTO = examRegistrationMapper.toDto(examRegistration);

        @SuppressWarnings("unchecked")
        HttpResponse<ExamRegistrationDTO> response = client.exchange(HttpRequest.POST("/api/exam-registrations", examRegistrationDTO), ExamRegistrationDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamRegistrationDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<ExamRegistration> examRegistrationList = examRegistrationRepository.findAll();
        assertThat(examRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllExamRegistrations() throws Exception {
        // Initialize the database
        examRegistrationRepository.saveAndFlush(examRegistration);

        // Get the examRegistrationList w/ all the examRegistrations
        List<ExamRegistrationDTO> examRegistrations = client.retrieve(HttpRequest.GET("/api/exam-registrations?eagerload=true"), Argument.listOf(ExamRegistrationDTO.class)).blockingFirst();
        ExamRegistrationDTO testExamRegistration = examRegistrations.get(0);


        assertThat(testExamRegistration.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getExamRegistration() throws Exception {
        // Initialize the database
        examRegistrationRepository.saveAndFlush(examRegistration);

        // Get the examRegistration
        ExamRegistrationDTO testExamRegistration = client.retrieve(HttpRequest.GET("/api/exam-registrations/" + examRegistration.getId()), ExamRegistrationDTO.class).blockingFirst();


        assertThat(testExamRegistration.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getNonExistingExamRegistration() throws Exception {
        // Get the examRegistration
        @SuppressWarnings("unchecked")
        HttpResponse<ExamRegistrationDTO> response = client.exchange(HttpRequest.GET("/api/exam-registrations/"+ Long.MAX_VALUE), ExamRegistrationDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamRegistrationDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateExamRegistration() throws Exception {
        // Initialize the database
        examRegistrationRepository.saveAndFlush(examRegistration);

        int databaseSizeBeforeUpdate = examRegistrationRepository.findAll().size();

        // Update the examRegistration
        ExamRegistration updatedExamRegistration = examRegistrationRepository.findById(examRegistration.getId()).get();

        updatedExamRegistration
            .enrolmentDate(UPDATED_ENROLMENT_DATE);
        ExamRegistrationDTO updatedExamRegistrationDTO = examRegistrationMapper.toDto(updatedExamRegistration);

        @SuppressWarnings("unchecked")
        HttpResponse<ExamRegistrationDTO> response = client.exchange(HttpRequest.PUT("/api/exam-registrations", updatedExamRegistrationDTO), ExamRegistrationDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamRegistrationDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the ExamRegistration in the database
        List<ExamRegistration> examRegistrationList = examRegistrationRepository.findAll();
        assertThat(examRegistrationList).hasSize(databaseSizeBeforeUpdate);
        ExamRegistration testExamRegistration = examRegistrationList.get(examRegistrationList.size() - 1);

        assertThat(testExamRegistration.getEnrolmentDate()).isEqualTo(UPDATED_ENROLMENT_DATE);
    }

    @Test
    public void updateNonExistingExamRegistration() throws Exception {
        int databaseSizeBeforeUpdate = examRegistrationRepository.findAll().size();

        // Create the ExamRegistration
        ExamRegistrationDTO examRegistrationDTO = examRegistrationMapper.toDto(examRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<ExamRegistrationDTO> response = client.exchange(HttpRequest.PUT("/api/exam-registrations", examRegistrationDTO), ExamRegistrationDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamRegistrationDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the ExamRegistration in the database
        List<ExamRegistration> examRegistrationList = examRegistrationRepository.findAll();
        assertThat(examRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteExamRegistration() throws Exception {
        // Initialize the database with one entity
        examRegistrationRepository.saveAndFlush(examRegistration);

        int databaseSizeBeforeDelete = examRegistrationRepository.findAll().size();

        // Delete the examRegistration
        @SuppressWarnings("unchecked")
        HttpResponse<ExamRegistrationDTO> response = client.exchange(HttpRequest.DELETE("/api/exam-registrations/"+ examRegistration.getId()), ExamRegistrationDTO.class)
            .onErrorReturn(t -> (HttpResponse<ExamRegistrationDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<ExamRegistration> examRegistrationList = examRegistrationRepository.findAll();
        assertThat(examRegistrationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamRegistration.class);
        ExamRegistration examRegistration1 = new ExamRegistration();
        examRegistration1.setId(1L);
        ExamRegistration examRegistration2 = new ExamRegistration();
        examRegistration2.setId(examRegistration1.getId());
        assertThat(examRegistration1).isEqualTo(examRegistration2);
        examRegistration2.setId(2L);
        assertThat(examRegistration1).isNotEqualTo(examRegistration2);
        examRegistration1.setId(null);
        assertThat(examRegistration1).isNotEqualTo(examRegistration2);
    }
}
