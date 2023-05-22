package education.web.rest;


import education.domain.AnswerItem;
import education.repository.AnswerItemRepository;

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

import education.service.dto.AnswerItemDTO;
import education.service.mapper.AnswerItemMapper;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link AnswerItemResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnswerItemResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Inject
    private AnswerItemMapper answerItemMapper;
    @Inject
    private AnswerItemRepository answerItemRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private AnswerItem answerItem;

    @BeforeEach
    public void initTest() {
        answerItem = createEntity(transactionManager, em);
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
    public static AnswerItem createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        AnswerItem answerItem = new AnswerItem()
            .number(DEFAULT_NUMBER);
        return answerItem;
    }

    /**
     * Delete all answerItem entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, AnswerItem.class);
    }


    @Test
    public void createAnswerItem() throws Exception {
        int databaseSizeBeforeCreate = answerItemRepository.findAll().size();

        AnswerItemDTO answerItemDTO = answerItemMapper.toDto(answerItem);

        // Create the AnswerItem
        HttpResponse<AnswerItemDTO> response = client.exchange(HttpRequest.POST("/api/answer-items", answerItemDTO), AnswerItemDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the AnswerItem in the database
        List<AnswerItem> answerItemList = answerItemRepository.findAll();
        assertThat(answerItemList).hasSize(databaseSizeBeforeCreate + 1);
        AnswerItem testAnswerItem = answerItemList.get(answerItemList.size() - 1);

        assertThat(testAnswerItem.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void createAnswerItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = answerItemRepository.findAll().size();

        // Create the AnswerItem with an existing ID
        answerItem.setId(1L);
        AnswerItemDTO answerItemDTO = answerItemMapper.toDto(answerItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<AnswerItemDTO> response = client.exchange(HttpRequest.POST("/api/answer-items", answerItemDTO), AnswerItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<AnswerItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the AnswerItem in the database
        List<AnswerItem> answerItemList = answerItemRepository.findAll();
        assertThat(answerItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerItemRepository.findAll().size();
        // set the field null
        answerItem.setNumber(null);

        // Create the AnswerItem, which fails.
        AnswerItemDTO answerItemDTO = answerItemMapper.toDto(answerItem);

        @SuppressWarnings("unchecked")
        HttpResponse<AnswerItemDTO> response = client.exchange(HttpRequest.POST("/api/answer-items", answerItemDTO), AnswerItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<AnswerItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<AnswerItem> answerItemList = answerItemRepository.findAll();
        assertThat(answerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAnswerItems() throws Exception {
        // Initialize the database
        answerItemRepository.saveAndFlush(answerItem);

        // Get the answerItemList w/ all the answerItems
        List<AnswerItemDTO> answerItems = client.retrieve(HttpRequest.GET("/api/answer-items?eagerload=true"), Argument.listOf(AnswerItemDTO.class)).blockingFirst();
        AnswerItemDTO testAnswerItem = answerItems.get(0);


        assertThat(testAnswerItem.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void getAnswerItem() throws Exception {
        // Initialize the database
        answerItemRepository.saveAndFlush(answerItem);

        // Get the answerItem
        AnswerItemDTO testAnswerItem = client.retrieve(HttpRequest.GET("/api/answer-items/" + answerItem.getId()), AnswerItemDTO.class).blockingFirst();


        assertThat(testAnswerItem.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    public void getNonExistingAnswerItem() throws Exception {
        // Get the answerItem
        @SuppressWarnings("unchecked")
        HttpResponse<AnswerItemDTO> response = client.exchange(HttpRequest.GET("/api/answer-items/"+ Long.MAX_VALUE), AnswerItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<AnswerItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateAnswerItem() throws Exception {
        // Initialize the database
        answerItemRepository.saveAndFlush(answerItem);

        int databaseSizeBeforeUpdate = answerItemRepository.findAll().size();

        // Update the answerItem
        AnswerItem updatedAnswerItem = answerItemRepository.findById(answerItem.getId()).get();

        updatedAnswerItem
            .number(UPDATED_NUMBER);
        AnswerItemDTO updatedAnswerItemDTO = answerItemMapper.toDto(updatedAnswerItem);

        @SuppressWarnings("unchecked")
        HttpResponse<AnswerItemDTO> response = client.exchange(HttpRequest.PUT("/api/answer-items", updatedAnswerItemDTO), AnswerItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<AnswerItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the AnswerItem in the database
        List<AnswerItem> answerItemList = answerItemRepository.findAll();
        assertThat(answerItemList).hasSize(databaseSizeBeforeUpdate);
        AnswerItem testAnswerItem = answerItemList.get(answerItemList.size() - 1);

        assertThat(testAnswerItem.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    public void updateNonExistingAnswerItem() throws Exception {
        int databaseSizeBeforeUpdate = answerItemRepository.findAll().size();

        // Create the AnswerItem
        AnswerItemDTO answerItemDTO = answerItemMapper.toDto(answerItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<AnswerItemDTO> response = client.exchange(HttpRequest.PUT("/api/answer-items", answerItemDTO), AnswerItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<AnswerItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the AnswerItem in the database
        List<AnswerItem> answerItemList = answerItemRepository.findAll();
        assertThat(answerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAnswerItem() throws Exception {
        // Initialize the database with one entity
        answerItemRepository.saveAndFlush(answerItem);

        int databaseSizeBeforeDelete = answerItemRepository.findAll().size();

        // Delete the answerItem
        @SuppressWarnings("unchecked")
        HttpResponse<AnswerItemDTO> response = client.exchange(HttpRequest.DELETE("/api/answer-items/"+ answerItem.getId()), AnswerItemDTO.class)
            .onErrorReturn(t -> (HttpResponse<AnswerItemDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<AnswerItem> answerItemList = answerItemRepository.findAll();
        assertThat(answerItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerItem.class);
        AnswerItem answerItem1 = new AnswerItem();
        answerItem1.setId(1L);
        AnswerItem answerItem2 = new AnswerItem();
        answerItem2.setId(answerItem1.getId());
        assertThat(answerItem1).isEqualTo(answerItem2);
        answerItem2.setId(2L);
        assertThat(answerItem1).isNotEqualTo(answerItem2);
        answerItem1.setId(null);
        assertThat(answerItem1).isNotEqualTo(answerItem2);
    }
}
