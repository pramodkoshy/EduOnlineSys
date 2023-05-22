package education.web.rest;


import education.domain.Question;
import education.repository.QuestionRepository;

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

import education.service.dto.QuestionDTO;
import education.service.mapper.QuestionMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link QuestionResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Inject
    private QuestionMapper questionMapper;
    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private Question question;

    @BeforeEach
    public void initTest() {
        question = createEntity(transactionManager, em);
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
    public static Question createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Question question = new Question()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .number(DEFAULT_NUMBER);
        return question;
    }

    /**
     * Delete all question entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Question.class);
    }


    @Test
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        QuestionDTO questionDTO = questionMapper.toDto(question);

        // Create the Question
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.POST("/api/questions", questionDTO), QuestionDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);

        assertThat(testQuestion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuestion.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void createQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.POST("/api/questions", questionDTO), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setName(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.POST("/api/questions", questionDTO), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setDescription(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.POST("/api/questions", questionDTO), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setNumber(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.POST("/api/questions", questionDTO), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the questionList w/ all the questions
        List<QuestionDTO> questions = client.retrieve(HttpRequest.GET("/api/questions?eagerload=true"), Argument.listOf(QuestionDTO.class)).blockingFirst();
        QuestionDTO testQuestion = questions.get(0);


        assertThat(testQuestion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuestion.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        QuestionDTO testQuestion = client.retrieve(HttpRequest.GET("/api/questions/" + question.getId()), QuestionDTO.class).blockingFirst();


        assertThat(testQuestion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuestion.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.GET("/api/questions/"+ Long.MAX_VALUE), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();

        updatedQuestion
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .number(UPDATED_NUMBER);
        QuestionDTO updatedQuestionDTO = questionMapper.toDto(updatedQuestion);

        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.PUT("/api/questions", updatedQuestionDTO), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);

        assertThat(testQuestion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuestion.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    public void updateNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.PUT("/api/questions", questionDTO), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteQuestion() throws Exception {
        // Initialize the database with one entity
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Delete the question
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionDTO> response = client.exchange(HttpRequest.DELETE("/api/questions/"+ question.getId()), QuestionDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = new Question();
        question1.setId(1L);
        Question question2 = new Question();
        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);
        question2.setId(2L);
        assertThat(question1).isNotEqualTo(question2);
        question1.setId(null);
        assertThat(question1).isNotEqualTo(question2);
    }
}
