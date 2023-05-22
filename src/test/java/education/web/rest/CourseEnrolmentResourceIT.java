package education.web.rest;


import education.domain.CourseEnrolment;
import education.repository.CourseEnrolmentRepository;

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

import education.service.dto.CourseEnrolmentDTO;
import education.service.mapper.CourseEnrolmentMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link CourseEnrolmentResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseEnrolmentResourceIT {

    private static final LocalDate DEFAULT_ENROLMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENROLMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private CourseEnrolmentMapper courseEnrolmentMapper;
    @Inject
    private CourseEnrolmentRepository courseEnrolmentRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private CourseEnrolment courseEnrolment;

    @BeforeEach
    public void initTest() {
        courseEnrolment = createEntity(transactionManager, em);
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
    public static CourseEnrolment createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        CourseEnrolment courseEnrolment = new CourseEnrolment()
            .enrolmentDate(DEFAULT_ENROLMENT_DATE);
        return courseEnrolment;
    }

    /**
     * Delete all courseEnrolment entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, CourseEnrolment.class);
    }


    @Test
    public void createCourseEnrolment() throws Exception {
        int databaseSizeBeforeCreate = courseEnrolmentRepository.findAll().size();

        CourseEnrolmentDTO courseEnrolmentDTO = courseEnrolmentMapper.toDto(courseEnrolment);

        // Create the CourseEnrolment
        HttpResponse<CourseEnrolmentDTO> response = client.exchange(HttpRequest.POST("/api/course-enrolments", courseEnrolmentDTO), CourseEnrolmentDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the CourseEnrolment in the database
        List<CourseEnrolment> courseEnrolmentList = courseEnrolmentRepository.findAll();
        assertThat(courseEnrolmentList).hasSize(databaseSizeBeforeCreate + 1);
        CourseEnrolment testCourseEnrolment = courseEnrolmentList.get(courseEnrolmentList.size() - 1);

        assertThat(testCourseEnrolment.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void createCourseEnrolmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseEnrolmentRepository.findAll().size();

        // Create the CourseEnrolment with an existing ID
        courseEnrolment.setId(1L);
        CourseEnrolmentDTO courseEnrolmentDTO = courseEnrolmentMapper.toDto(courseEnrolment);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<CourseEnrolmentDTO> response = client.exchange(HttpRequest.POST("/api/course-enrolments", courseEnrolmentDTO), CourseEnrolmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseEnrolmentDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseEnrolment in the database
        List<CourseEnrolment> courseEnrolmentList = courseEnrolmentRepository.findAll();
        assertThat(courseEnrolmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkEnrolmentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseEnrolmentRepository.findAll().size();
        // set the field null
        courseEnrolment.setEnrolmentDate(null);

        // Create the CourseEnrolment, which fails.
        CourseEnrolmentDTO courseEnrolmentDTO = courseEnrolmentMapper.toDto(courseEnrolment);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseEnrolmentDTO> response = client.exchange(HttpRequest.POST("/api/course-enrolments", courseEnrolmentDTO), CourseEnrolmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseEnrolmentDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseEnrolment> courseEnrolmentList = courseEnrolmentRepository.findAll();
        assertThat(courseEnrolmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCourseEnrolments() throws Exception {
        // Initialize the database
        courseEnrolmentRepository.saveAndFlush(courseEnrolment);

        // Get the courseEnrolmentList w/ all the courseEnrolments
        List<CourseEnrolmentDTO> courseEnrolments = client.retrieve(HttpRequest.GET("/api/course-enrolments?eagerload=true"), Argument.listOf(CourseEnrolmentDTO.class)).blockingFirst();
        CourseEnrolmentDTO testCourseEnrolment = courseEnrolments.get(0);


        assertThat(testCourseEnrolment.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getCourseEnrolment() throws Exception {
        // Initialize the database
        courseEnrolmentRepository.saveAndFlush(courseEnrolment);

        // Get the courseEnrolment
        CourseEnrolmentDTO testCourseEnrolment = client.retrieve(HttpRequest.GET("/api/course-enrolments/" + courseEnrolment.getId()), CourseEnrolmentDTO.class).blockingFirst();


        assertThat(testCourseEnrolment.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getNonExistingCourseEnrolment() throws Exception {
        // Get the courseEnrolment
        @SuppressWarnings("unchecked")
        HttpResponse<CourseEnrolmentDTO> response = client.exchange(HttpRequest.GET("/api/course-enrolments/"+ Long.MAX_VALUE), CourseEnrolmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseEnrolmentDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateCourseEnrolment() throws Exception {
        // Initialize the database
        courseEnrolmentRepository.saveAndFlush(courseEnrolment);

        int databaseSizeBeforeUpdate = courseEnrolmentRepository.findAll().size();

        // Update the courseEnrolment
        CourseEnrolment updatedCourseEnrolment = courseEnrolmentRepository.findById(courseEnrolment.getId()).get();

        updatedCourseEnrolment
            .enrolmentDate(UPDATED_ENROLMENT_DATE);
        CourseEnrolmentDTO updatedCourseEnrolmentDTO = courseEnrolmentMapper.toDto(updatedCourseEnrolment);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseEnrolmentDTO> response = client.exchange(HttpRequest.PUT("/api/course-enrolments", updatedCourseEnrolmentDTO), CourseEnrolmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseEnrolmentDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the CourseEnrolment in the database
        List<CourseEnrolment> courseEnrolmentList = courseEnrolmentRepository.findAll();
        assertThat(courseEnrolmentList).hasSize(databaseSizeBeforeUpdate);
        CourseEnrolment testCourseEnrolment = courseEnrolmentList.get(courseEnrolmentList.size() - 1);

        assertThat(testCourseEnrolment.getEnrolmentDate()).isEqualTo(UPDATED_ENROLMENT_DATE);
    }

    @Test
    public void updateNonExistingCourseEnrolment() throws Exception {
        int databaseSizeBeforeUpdate = courseEnrolmentRepository.findAll().size();

        // Create the CourseEnrolment
        CourseEnrolmentDTO courseEnrolmentDTO = courseEnrolmentMapper.toDto(courseEnrolment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<CourseEnrolmentDTO> response = client.exchange(HttpRequest.PUT("/api/course-enrolments", courseEnrolmentDTO), CourseEnrolmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseEnrolmentDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseEnrolment in the database
        List<CourseEnrolment> courseEnrolmentList = courseEnrolmentRepository.findAll();
        assertThat(courseEnrolmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCourseEnrolment() throws Exception {
        // Initialize the database with one entity
        courseEnrolmentRepository.saveAndFlush(courseEnrolment);

        int databaseSizeBeforeDelete = courseEnrolmentRepository.findAll().size();

        // Delete the courseEnrolment
        @SuppressWarnings("unchecked")
        HttpResponse<CourseEnrolmentDTO> response = client.exchange(HttpRequest.DELETE("/api/course-enrolments/"+ courseEnrolment.getId()), CourseEnrolmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseEnrolmentDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<CourseEnrolment> courseEnrolmentList = courseEnrolmentRepository.findAll();
        assertThat(courseEnrolmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseEnrolment.class);
        CourseEnrolment courseEnrolment1 = new CourseEnrolment();
        courseEnrolment1.setId(1L);
        CourseEnrolment courseEnrolment2 = new CourseEnrolment();
        courseEnrolment2.setId(courseEnrolment1.getId());
        assertThat(courseEnrolment1).isEqualTo(courseEnrolment2);
        courseEnrolment2.setId(2L);
        assertThat(courseEnrolment1).isNotEqualTo(courseEnrolment2);
        courseEnrolment1.setId(null);
        assertThat(courseEnrolment1).isNotEqualTo(courseEnrolment2);
    }
}
