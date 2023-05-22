package education.web.rest;


import education.domain.CourseLearningObjects;
import education.repository.CourseLearningObjectsRepository;

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

import education.service.dto.CourseLearningObjectsDTO;
import education.service.mapper.CourseLearningObjectsMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link CourseLearningObjectsResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseLearningObjectsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Inject
    private CourseLearningObjectsMapper courseLearningObjectsMapper;
    @Inject
    private CourseLearningObjectsRepository courseLearningObjectsRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private CourseLearningObjects courseLearningObjects;

    @BeforeEach
    public void initTest() {
        courseLearningObjects = createEntity(transactionManager, em);
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
    public static CourseLearningObjects createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        CourseLearningObjects courseLearningObjects = new CourseLearningObjects()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .text(DEFAULT_TEXT)
            .url(DEFAULT_URL);
        return courseLearningObjects;
    }

    /**
     * Delete all courseLearningObjects entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, CourseLearningObjects.class);
    }


    @Test
    public void createCourseLearningObjects() throws Exception {
        int databaseSizeBeforeCreate = courseLearningObjectsRepository.findAll().size();

        CourseLearningObjectsDTO courseLearningObjectsDTO = courseLearningObjectsMapper.toDto(courseLearningObjects);

        // Create the CourseLearningObjects
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.POST("/api/course-learning-objects", courseLearningObjectsDTO), CourseLearningObjectsDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the CourseLearningObjects in the database
        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeCreate + 1);
        CourseLearningObjects testCourseLearningObjects = courseLearningObjectsList.get(courseLearningObjectsList.size() - 1);

        assertThat(testCourseLearningObjects.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseLearningObjects.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseLearningObjects.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testCourseLearningObjects.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    public void createCourseLearningObjectsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseLearningObjectsRepository.findAll().size();

        // Create the CourseLearningObjects with an existing ID
        courseLearningObjects.setId(1L);
        CourseLearningObjectsDTO courseLearningObjectsDTO = courseLearningObjectsMapper.toDto(courseLearningObjects);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.POST("/api/course-learning-objects", courseLearningObjectsDTO), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseLearningObjects in the database
        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLearningObjectsRepository.findAll().size();
        // set the field null
        courseLearningObjects.setName(null);

        // Create the CourseLearningObjects, which fails.
        CourseLearningObjectsDTO courseLearningObjectsDTO = courseLearningObjectsMapper.toDto(courseLearningObjects);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.POST("/api/course-learning-objects", courseLearningObjectsDTO), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLearningObjectsRepository.findAll().size();
        // set the field null
        courseLearningObjects.setType(null);

        // Create the CourseLearningObjects, which fails.
        CourseLearningObjectsDTO courseLearningObjectsDTO = courseLearningObjectsMapper.toDto(courseLearningObjects);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.POST("/api/course-learning-objects", courseLearningObjectsDTO), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLearningObjectsRepository.findAll().size();
        // set the field null
        courseLearningObjects.setUrl(null);

        // Create the CourseLearningObjects, which fails.
        CourseLearningObjectsDTO courseLearningObjectsDTO = courseLearningObjectsMapper.toDto(courseLearningObjects);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.POST("/api/course-learning-objects", courseLearningObjectsDTO), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCourseLearningObjects() throws Exception {
        // Initialize the database
        courseLearningObjectsRepository.saveAndFlush(courseLearningObjects);

        // Get the courseLearningObjectsList w/ all the courseLearningObjects
        List<CourseLearningObjectsDTO> courseLearningObjects = client.retrieve(HttpRequest.GET("/api/course-learning-objects?eagerload=true"), Argument.listOf(CourseLearningObjectsDTO.class)).blockingFirst();
        CourseLearningObjectsDTO testCourseLearningObjects = courseLearningObjects.get(0);


        assertThat(testCourseLearningObjects.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseLearningObjects.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseLearningObjects.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testCourseLearningObjects.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    public void getCourseLearningObjects() throws Exception {
        // Initialize the database
        courseLearningObjectsRepository.saveAndFlush(courseLearningObjects);

        // Get the courseLearningObjects
        CourseLearningObjectsDTO testCourseLearningObjects = client.retrieve(HttpRequest.GET("/api/course-learning-objects/" + courseLearningObjects.getId()), CourseLearningObjectsDTO.class).blockingFirst();


        assertThat(testCourseLearningObjects.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseLearningObjects.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseLearningObjects.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testCourseLearningObjects.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    public void getNonExistingCourseLearningObjects() throws Exception {
        // Get the courseLearningObjects
        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.GET("/api/course-learning-objects/"+ Long.MAX_VALUE), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateCourseLearningObjects() throws Exception {
        // Initialize the database
        courseLearningObjectsRepository.saveAndFlush(courseLearningObjects);

        int databaseSizeBeforeUpdate = courseLearningObjectsRepository.findAll().size();

        // Update the courseLearningObjects
        CourseLearningObjects updatedCourseLearningObjects = courseLearningObjectsRepository.findById(courseLearningObjects.getId()).get();

        updatedCourseLearningObjects
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .text(UPDATED_TEXT)
            .url(UPDATED_URL);
        CourseLearningObjectsDTO updatedCourseLearningObjectsDTO = courseLearningObjectsMapper.toDto(updatedCourseLearningObjects);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.PUT("/api/course-learning-objects", updatedCourseLearningObjectsDTO), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the CourseLearningObjects in the database
        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeUpdate);
        CourseLearningObjects testCourseLearningObjects = courseLearningObjectsList.get(courseLearningObjectsList.size() - 1);

        assertThat(testCourseLearningObjects.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourseLearningObjects.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCourseLearningObjects.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCourseLearningObjects.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    public void updateNonExistingCourseLearningObjects() throws Exception {
        int databaseSizeBeforeUpdate = courseLearningObjectsRepository.findAll().size();

        // Create the CourseLearningObjects
        CourseLearningObjectsDTO courseLearningObjectsDTO = courseLearningObjectsMapper.toDto(courseLearningObjects);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.PUT("/api/course-learning-objects", courseLearningObjectsDTO), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseLearningObjects in the database
        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCourseLearningObjects() throws Exception {
        // Initialize the database with one entity
        courseLearningObjectsRepository.saveAndFlush(courseLearningObjects);

        int databaseSizeBeforeDelete = courseLearningObjectsRepository.findAll().size();

        // Delete the courseLearningObjects
        @SuppressWarnings("unchecked")
        HttpResponse<CourseLearningObjectsDTO> response = client.exchange(HttpRequest.DELETE("/api/course-learning-objects/"+ courseLearningObjects.getId()), CourseLearningObjectsDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseLearningObjectsDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<CourseLearningObjects> courseLearningObjectsList = courseLearningObjectsRepository.findAll();
        assertThat(courseLearningObjectsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseLearningObjects.class);
        CourseLearningObjects courseLearningObjects1 = new CourseLearningObjects();
        courseLearningObjects1.setId(1L);
        CourseLearningObjects courseLearningObjects2 = new CourseLearningObjects();
        courseLearningObjects2.setId(courseLearningObjects1.getId());
        assertThat(courseLearningObjects1).isEqualTo(courseLearningObjects2);
        courseLearningObjects2.setId(2L);
        assertThat(courseLearningObjects1).isNotEqualTo(courseLearningObjects2);
        courseLearningObjects1.setId(null);
        assertThat(courseLearningObjects1).isNotEqualTo(courseLearningObjects2);
    }
}
