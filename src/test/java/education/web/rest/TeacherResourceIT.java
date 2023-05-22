package education.web.rest;


import education.domain.Teacher;
import education.repository.TeacherRepository;

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

import education.service.dto.TeacherDTO;
import education.service.mapper.TeacherMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link TeacherResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeacherResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATIONS = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATIONS = "BBBBBBBBBB";

    @Inject
    private TeacherMapper teacherMapper;
    @Inject
    private TeacherRepository teacherRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private Teacher teacher;

    @BeforeEach
    public void initTest() {
        teacher = createEntity(transactionManager, em);
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
    public static Teacher createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Teacher teacher = new Teacher()
            .name(DEFAULT_NAME)
            .qualifications(DEFAULT_QUALIFICATIONS);
        return teacher;
    }

    /**
     * Delete all teacher entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Teacher.class);
    }


    @Test
    public void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // Create the Teacher
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.POST("/api/teachers", teacherDTO), TeacherDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate + 1);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);

        assertThat(testTeacher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeacher.getQualifications()).isEqualTo(DEFAULT_QUALIFICATIONS);
    }

    @Test
    public void createTeacherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // Create the Teacher with an existing ID
        teacher.setId(1L);
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.POST("/api/teachers", teacherDTO), TeacherDTO.class)
            .onErrorReturn(t -> (HttpResponse<TeacherDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setName(null);

        // Create the Teacher, which fails.
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        @SuppressWarnings("unchecked")
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.POST("/api/teachers", teacherDTO), TeacherDTO.class)
            .onErrorReturn(t -> (HttpResponse<TeacherDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkQualificationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setQualifications(null);

        // Create the Teacher, which fails.
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        @SuppressWarnings("unchecked")
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.POST("/api/teachers", teacherDTO), TeacherDTO.class)
            .onErrorReturn(t -> (HttpResponse<TeacherDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTeachers() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacherList w/ all the teachers
        List<TeacherDTO> teachers = client.retrieve(HttpRequest.GET("/api/teachers?eagerload=true"), Argument.listOf(TeacherDTO.class)).blockingFirst();
        TeacherDTO testTeacher = teachers.get(0);


        assertThat(testTeacher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeacher.getQualifications()).isEqualTo(DEFAULT_QUALIFICATIONS);
    }

    @Test
    public void getTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        TeacherDTO testTeacher = client.retrieve(HttpRequest.GET("/api/teachers/" + teacher.getId()), TeacherDTO.class).blockingFirst();


        assertThat(testTeacher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeacher.getQualifications()).isEqualTo(DEFAULT_QUALIFICATIONS);
    }

    @Test
    public void getNonExistingTeacher() throws Exception {
        // Get the teacher
        @SuppressWarnings("unchecked")
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.GET("/api/teachers/"+ Long.MAX_VALUE), TeacherDTO.class)
            .onErrorReturn(t -> (HttpResponse<TeacherDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getId()).get();

        updatedTeacher
            .name(UPDATED_NAME)
            .qualifications(UPDATED_QUALIFICATIONS);
        TeacherDTO updatedTeacherDTO = teacherMapper.toDto(updatedTeacher);

        @SuppressWarnings("unchecked")
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.PUT("/api/teachers", updatedTeacherDTO), TeacherDTO.class)
            .onErrorReturn(t -> (HttpResponse<TeacherDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);

        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getQualifications()).isEqualTo(UPDATED_QUALIFICATIONS);
    }

    @Test
    public void updateNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.PUT("/api/teachers", teacherDTO), TeacherDTO.class)
            .onErrorReturn(t -> (HttpResponse<TeacherDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTeacher() throws Exception {
        // Initialize the database with one entity
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Delete the teacher
        @SuppressWarnings("unchecked")
        HttpResponse<TeacherDTO> response = client.exchange(HttpRequest.DELETE("/api/teachers/"+ teacher.getId()), TeacherDTO.class)
            .onErrorReturn(t -> (HttpResponse<TeacherDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Teacher.class);
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(teacher1.getId());
        assertThat(teacher1).isEqualTo(teacher2);
        teacher2.setId(2L);
        assertThat(teacher1).isNotEqualTo(teacher2);
        teacher1.setId(null);
        assertThat(teacher1).isNotEqualTo(teacher2);
    }
}
