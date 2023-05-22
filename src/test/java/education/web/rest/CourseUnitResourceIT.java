package education.web.rest;


import education.domain.CourseUnit;
import education.repository.CourseUnitRepository;

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

import education.service.dto.CourseUnitDTO;
import education.service.mapper.CourseUnitMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link CourseUnitResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseUnitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private CourseUnitMapper courseUnitMapper;
    @Inject
    private CourseUnitRepository courseUnitRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private CourseUnit courseUnit;

    @BeforeEach
    public void initTest() {
        courseUnit = createEntity(transactionManager, em);
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
    public static CourseUnit createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        CourseUnit courseUnit = new CourseUnit()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return courseUnit;
    }

    /**
     * Delete all courseUnit entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, CourseUnit.class);
    }


    @Test
    public void createCourseUnit() throws Exception {
        int databaseSizeBeforeCreate = courseUnitRepository.findAll().size();

        CourseUnitDTO courseUnitDTO = courseUnitMapper.toDto(courseUnit);

        // Create the CourseUnit
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.POST("/api/course-units", courseUnitDTO), CourseUnitDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the CourseUnit in the database
        List<CourseUnit> courseUnitList = courseUnitRepository.findAll();
        assertThat(courseUnitList).hasSize(databaseSizeBeforeCreate + 1);
        CourseUnit testCourseUnit = courseUnitList.get(courseUnitList.size() - 1);

        assertThat(testCourseUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void createCourseUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseUnitRepository.findAll().size();

        // Create the CourseUnit with an existing ID
        courseUnit.setId(1L);
        CourseUnitDTO courseUnitDTO = courseUnitMapper.toDto(courseUnit);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.POST("/api/course-units", courseUnitDTO), CourseUnitDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseUnitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseUnit in the database
        List<CourseUnit> courseUnitList = courseUnitRepository.findAll();
        assertThat(courseUnitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseUnitRepository.findAll().size();
        // set the field null
        courseUnit.setName(null);

        // Create the CourseUnit, which fails.
        CourseUnitDTO courseUnitDTO = courseUnitMapper.toDto(courseUnit);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.POST("/api/course-units", courseUnitDTO), CourseUnitDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseUnitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseUnit> courseUnitList = courseUnitRepository.findAll();
        assertThat(courseUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseUnitRepository.findAll().size();
        // set the field null
        courseUnit.setDescription(null);

        // Create the CourseUnit, which fails.
        CourseUnitDTO courseUnitDTO = courseUnitMapper.toDto(courseUnit);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.POST("/api/course-units", courseUnitDTO), CourseUnitDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseUnitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseUnit> courseUnitList = courseUnitRepository.findAll();
        assertThat(courseUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCourseUnits() throws Exception {
        // Initialize the database
        courseUnitRepository.saveAndFlush(courseUnit);

        // Get the courseUnitList w/ all the courseUnits
        List<CourseUnitDTO> courseUnits = client.retrieve(HttpRequest.GET("/api/course-units?eagerload=true"), Argument.listOf(CourseUnitDTO.class)).blockingFirst();
        CourseUnitDTO testCourseUnit = courseUnits.get(0);


        assertThat(testCourseUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void getCourseUnit() throws Exception {
        // Initialize the database
        courseUnitRepository.saveAndFlush(courseUnit);

        // Get the courseUnit
        CourseUnitDTO testCourseUnit = client.retrieve(HttpRequest.GET("/api/course-units/" + courseUnit.getId()), CourseUnitDTO.class).blockingFirst();


        assertThat(testCourseUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void getNonExistingCourseUnit() throws Exception {
        // Get the courseUnit
        @SuppressWarnings("unchecked")
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.GET("/api/course-units/"+ Long.MAX_VALUE), CourseUnitDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseUnitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateCourseUnit() throws Exception {
        // Initialize the database
        courseUnitRepository.saveAndFlush(courseUnit);

        int databaseSizeBeforeUpdate = courseUnitRepository.findAll().size();

        // Update the courseUnit
        CourseUnit updatedCourseUnit = courseUnitRepository.findById(courseUnit.getId()).get();

        updatedCourseUnit
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        CourseUnitDTO updatedCourseUnitDTO = courseUnitMapper.toDto(updatedCourseUnit);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.PUT("/api/course-units", updatedCourseUnitDTO), CourseUnitDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseUnitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the CourseUnit in the database
        List<CourseUnit> courseUnitList = courseUnitRepository.findAll();
        assertThat(courseUnitList).hasSize(databaseSizeBeforeUpdate);
        CourseUnit testCourseUnit = courseUnitList.get(courseUnitList.size() - 1);

        assertThat(testCourseUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourseUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    public void updateNonExistingCourseUnit() throws Exception {
        int databaseSizeBeforeUpdate = courseUnitRepository.findAll().size();

        // Create the CourseUnit
        CourseUnitDTO courseUnitDTO = courseUnitMapper.toDto(courseUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.PUT("/api/course-units", courseUnitDTO), CourseUnitDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseUnitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseUnit in the database
        List<CourseUnit> courseUnitList = courseUnitRepository.findAll();
        assertThat(courseUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCourseUnit() throws Exception {
        // Initialize the database with one entity
        courseUnitRepository.saveAndFlush(courseUnit);

        int databaseSizeBeforeDelete = courseUnitRepository.findAll().size();

        // Delete the courseUnit
        @SuppressWarnings("unchecked")
        HttpResponse<CourseUnitDTO> response = client.exchange(HttpRequest.DELETE("/api/course-units/"+ courseUnit.getId()), CourseUnitDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseUnitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<CourseUnit> courseUnitList = courseUnitRepository.findAll();
        assertThat(courseUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseUnit.class);
        CourseUnit courseUnit1 = new CourseUnit();
        courseUnit1.setId(1L);
        CourseUnit courseUnit2 = new CourseUnit();
        courseUnit2.setId(courseUnit1.getId());
        assertThat(courseUnit1).isEqualTo(courseUnit2);
        courseUnit2.setId(2L);
        assertThat(courseUnit1).isNotEqualTo(courseUnit2);
        courseUnit1.setId(null);
        assertThat(courseUnit1).isNotEqualTo(courseUnit2);
    }
}
