package education.web.rest;


import education.domain.QuestionItem;
import education.repository.QuestionItemRepository;

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

import education.service.dto.QuestionItemDTO;
import education.service.mapper.QuestionItemMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link QuestionItemResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Inject
    private QuestionItemMapper questionItemMapper;
    @Inject
    private QuestionItemRepository questionItemRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private QuestionItem questionItem;

    @BeforeEach
    public void initTest() {
        questionItem = createEntity(transactionManager, em);
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
    public static QuestionItem createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        QuestionItem questionItem = new QuestionItem()
            .name(DEFAULT_NAME)
            .number(DEFAULT_NUMBER);
        return questionItem;
    }

    /**
     * Delete all questionItem entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, QuestionItem.class);
    }


    @Test
    public void createQuestionItem() throws Exception {
        int databaseSizeBeforeCreate = questionItemRepository.findAll().size();

        QuestionItemDTO questionItemDTO = questionItemMapper.toDto(questionItem);

        // Create the QuestionItem
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.POST("/api/question-items", questionItemDTO), QuestionItemDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the QuestionItem in the database
        List<QuestionItem> questionItemList = questionItemRepository.findAll();
        assertThat(questionItemList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionItem testQuestionItem = questionItemList.get(questionItemList.size() - 1);

        assertThat(testQuestionItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionItem.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void createQuestionItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionItemRepository.findAll().size();

        // Create the QuestionItem with an existing ID
        questionItem.setId(1L);
        QuestionItemDTO questionItemDTO = questionItemMapper.toDto(questionItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.POST("/api/question-items", questionItemDTO), QuestionItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the QuestionItem in the database
        List<QuestionItem> questionItemList = questionItemRepository.findAll();
        assertThat(questionItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionItemRepository.findAll().size();
        // set the field null
        questionItem.setName(null);

        // Create the QuestionItem, which fails.
        QuestionItemDTO questionItemDTO = questionItemMapper.toDto(questionItem);

        @SuppressWarnings("unchecked")
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.POST("/api/question-items", questionItemDTO), QuestionItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<QuestionItem> questionItemList = questionItemRepository.findAll();
        assertThat(questionItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionItemRepository.findAll().size();
        // set the field null
        questionItem.setNumber(null);

        // Create the QuestionItem, which fails.
        QuestionItemDTO questionItemDTO = questionItemMapper.toDto(questionItem);

        @SuppressWarnings("unchecked")
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.POST("/api/question-items", questionItemDTO), QuestionItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<QuestionItem> questionItemList = questionItemRepository.findAll();
        assertThat(questionItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllQuestionItems() throws Exception {
        // Initialize the database
        questionItemRepository.saveAndFlush(questionItem);

        // Get the questionItemList w/ all the questionItems
        List<QuestionItemDTO> questionItems = client.retrieve(HttpRequest.GET("/api/question-items?eagerload=true"), Argument.listOf(QuestionItemDTO.class)).blockingFirst();
        QuestionItemDTO testQuestionItem = questionItems.get(0);


        assertThat(testQuestionItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionItem.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void getQuestionItem() throws Exception {
        // Initialize the database
        questionItemRepository.saveAndFlush(questionItem);

        // Get the questionItem
        QuestionItemDTO testQuestionItem = client.retrieve(HttpRequest.GET("/api/question-items/" + questionItem.getId()), QuestionItemDTO.class).blockingFirst();


        assertThat(testQuestionItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionItem.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void getNonExistingQuestionItem() throws Exception {
        // Get the questionItem
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.GET("/api/question-items/"+ Long.MAX_VALUE), QuestionItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateQuestionItem() throws Exception {
        // Initialize the database
        questionItemRepository.saveAndFlush(questionItem);

        int databaseSizeBeforeUpdate = questionItemRepository.findAll().size();

        // Update the questionItem
        QuestionItem updatedQuestionItem = questionItemRepository.findById(questionItem.getId()).get();

        updatedQuestionItem
            .name(UPDATED_NAME)
            .number(UPDATED_NUMBER);
        QuestionItemDTO updatedQuestionItemDTO = questionItemMapper.toDto(updatedQuestionItem);

        @SuppressWarnings("unchecked")
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.PUT("/api/question-items", updatedQuestionItemDTO), QuestionItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the QuestionItem in the database
        List<QuestionItem> questionItemList = questionItemRepository.findAll();
        assertThat(questionItemList).hasSize(databaseSizeBeforeUpdate);
        QuestionItem testQuestionItem = questionItemList.get(questionItemList.size() - 1);

        assertThat(testQuestionItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionItem.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    public void updateNonExistingQuestionItem() throws Exception {
        int databaseSizeBeforeUpdate = questionItemRepository.findAll().size();

        // Create the QuestionItem
        QuestionItemDTO questionItemDTO = questionItemMapper.toDto(questionItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.PUT("/api/question-items", questionItemDTO), QuestionItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the QuestionItem in the database
        List<QuestionItem> questionItemList = questionItemRepository.findAll();
        assertThat(questionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteQuestionItem() throws Exception {
        // Initialize the database with one entity
        questionItemRepository.saveAndFlush(questionItem);

        int databaseSizeBeforeDelete = questionItemRepository.findAll().size();

        // Delete the questionItem
        @SuppressWarnings("unchecked")
        HttpResponse<QuestionItemDTO> response = client.exchange(HttpRequest.DELETE("/api/question-items/"+ questionItem.getId()), QuestionItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<QuestionItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<QuestionItem> questionItemList = questionItemRepository.findAll();
        assertThat(questionItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionItem.class);
        QuestionItem questionItem1 = new QuestionItem();
        questionItem1.setId(1L);
        QuestionItem questionItem2 = new QuestionItem();
        questionItem2.setId(questionItem1.getId());
        assertThat(questionItem1).isEqualTo(questionItem2);
        questionItem2.setId(2L);
        assertThat(questionItem1).isNotEqualTo(questionItem2);
        questionItem1.setId(null);
        assertThat(questionItem1).isNotEqualTo(questionItem2);
    }
}
