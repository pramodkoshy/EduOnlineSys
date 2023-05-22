package education.web.rest;


import education.domain.CourseModule;
import education.repository.CourseModuleRepository;

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

import education.service.dto.CourseModuleDTO;
import education.service.mapper.CourseModuleMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link CourseModuleResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseModuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private CourseModuleMapper courseModuleMapper;
    @Inject
    private CourseModuleRepository courseModuleRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private CourseModule courseModule;

    @BeforeEach
    public void initTest() {
        courseModule = createEntity(transactionManager, em);
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
    public static CourseModule createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        CourseModule courseModule = new CourseModule()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return courseModule;
    }

    /**
     * Delete all courseModule entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, CourseModule.class);
    }


    @Test
    public void createCourseModule() throws Exception {
        int databaseSizeBeforeCreate = courseModuleRepository.findAll().size();

        CourseModuleDTO courseModuleDTO = courseModuleMapper.toDto(courseModule);

        // Create the CourseModule
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.POST("/api/course-modules", courseModuleDTO), CourseModuleDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the CourseModule in the database
        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeCreate + 1);
        CourseModule testCourseModule = courseModuleList.get(courseModuleList.size() - 1);

        assertThat(testCourseModule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseModule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourseModule.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourseModule.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    public void createCourseModuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseModuleRepository.findAll().size();

        // Create the CourseModule with an existing ID
        courseModule.setId(1L);
        CourseModuleDTO courseModuleDTO = courseModuleMapper.toDto(courseModule);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.POST("/api/course-modules", courseModuleDTO), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseModule in the database
        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseModuleRepository.findAll().size();
        // set the field null
        courseModule.setName(null);

        // Create the CourseModule, which fails.
        CourseModuleDTO courseModuleDTO = courseModuleMapper.toDto(courseModule);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.POST("/api/course-modules", courseModuleDTO), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseModuleRepository.findAll().size();
        // set the field null
        courseModule.setDescription(null);

        // Create the CourseModule, which fails.
        CourseModuleDTO courseModuleDTO = courseModuleMapper.toDto(courseModule);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.POST("/api/course-modules", courseModuleDTO), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseModuleRepository.findAll().size();
        // set the field null
        courseModule.setStartDate(null);

        // Create the CourseModule, which fails.
        CourseModuleDTO courseModuleDTO = courseModuleMapper.toDto(courseModule);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.POST("/api/course-modules", courseModuleDTO), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseModuleRepository.findAll().size();
        // set the field null
        courseModule.setEndDate(null);

        // Create the CourseModule, which fails.
        CourseModuleDTO courseModuleDTO = courseModuleMapper.toDto(courseModule);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.POST("/api/course-modules", courseModuleDTO), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCourseModules() throws Exception {
        // Initialize the database
        courseModuleRepository.saveAndFlush(courseModule);

        // Get the courseModuleList w/ all the courseModules
        List<CourseModuleDTO> courseModules = client.retrieve(HttpRequest.GET("/api/course-modules?eagerload=true"), Argument.listOf(CourseModuleDTO.class)).blockingFirst();
        CourseModuleDTO testCourseModule = courseModules.get(0);


        assertThat(testCourseModule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseModule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourseModule.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourseModule.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    public void getCourseModule() throws Exception {
        // Initialize the database
        courseModuleRepository.saveAndFlush(courseModule);

        // Get the courseModule
        CourseModuleDTO testCourseModule = client.retrieve(HttpRequest.GET("/api/course-modules/" + courseModule.getId()), CourseModuleDTO.class).blockingFirst();


        assertThat(testCourseModule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseModule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourseModule.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourseModule.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    public void getNonExistingCourseModule() throws Exception {
        // Get the courseModule
        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.GET("/api/course-modules/"+ Long.MAX_VALUE), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateCourseModule() throws Exception {
        // Initialize the database
        courseModuleRepository.saveAndFlush(courseModule);

        int databaseSizeBeforeUpdate = courseModuleRepository.findAll().size();

        // Update the courseModule
        CourseModule updatedCourseModule = courseModuleRepository.findById(courseModule.getId()).get();

        updatedCourseModule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        CourseModuleDTO updatedCourseModuleDTO = courseModuleMapper.toDto(updatedCourseModule);

        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.PUT("/api/course-modules", updatedCourseModuleDTO), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the CourseModule in the database
        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeUpdate);
        CourseModule testCourseModule = courseModuleList.get(courseModuleList.size() - 1);

        assertThat(testCourseModule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourseModule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourseModule.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCourseModule.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    public void updateNonExistingCourseModule() throws Exception {
        int databaseSizeBeforeUpdate = courseModuleRepository.findAll().size();

        // Create the CourseModule
        CourseModuleDTO courseModuleDTO = courseModuleMapper.toDto(courseModule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.PUT("/api/course-modules", courseModuleDTO), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the CourseModule in the database
        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCourseModule() throws Exception {
        // Initialize the database with one entity
        courseModuleRepository.saveAndFlush(courseModule);

        int databaseSizeBeforeDelete = courseModuleRepository.findAll().size();

        // Delete the courseModule
        @SuppressWarnings("unchecked")
        HttpResponse<CourseModuleDTO> response = client.exchange(HttpRequest.DELETE("/api/course-modules/"+ courseModule.getId()), CourseModuleDTO.class)
            .onErrorReturn(t -> (HttpResponse<CourseModuleDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<CourseModule> courseModuleList = courseModuleRepository.findAll();
        assertThat(courseModuleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseModule.class);
        CourseModule courseModule1 = new CourseModule();
        courseModule1.setId(1L);
        CourseModule courseModule2 = new CourseModule();
        courseModule2.setId(courseModule1.getId());
        assertThat(courseModule1).isEqualTo(courseModule2);
        courseModule2.setId(2L);
        assertThat(courseModule1).isNotEqualTo(courseModule2);
        courseModule1.setId(null);
        assertThat(courseModule1).isNotEqualTo(courseModule2);
    }
}
