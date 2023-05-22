package education.web.rest;

import education.service.QuestionItemService;
import education.web.rest.errors.BadRequestAlertException;
import education.service.dto.QuestionItemDTO;

import education.util.HeaderUtil;
import education.util.PaginationUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;




import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link education.domain.QuestionItem}.
 */
@Controller("/api")
public class QuestionItemResource {

    private final Logger log = LoggerFactory.getLogger(QuestionItemResource.class);

    private static final String ENTITY_NAME = "questionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionItemService questionItemService;

    public QuestionItemResource(QuestionItemService questionItemService) {
        this.questionItemService = questionItemService;
    }

    /**
     * {@code POST  /question-items} : Create a new questionItem.
     *
     * @param questionItemDTO the questionItemDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new questionItemDTO, or with status {@code 400 (Bad Request)} if the questionItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/question-items")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<QuestionItemDTO> createQuestionItem(@Body QuestionItemDTO questionItemDTO) throws URISyntaxException {
        log.debug("REST request to save QuestionItem : {}", questionItemDTO);
        if (questionItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionItemDTO result = questionItemService.save(questionItemDTO);
        URI location = new URI("/api/question-items/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /question-items} : Updates an existing questionItem.
     *
     * @param questionItemDTO the questionItemDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated questionItemDTO,
     * or with status {@code 400 (Bad Request)} if the questionItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/question-items")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<QuestionItemDTO> updateQuestionItem(@Body QuestionItemDTO questionItemDTO) throws URISyntaxException {
        log.debug("REST request to update QuestionItem : {}", questionItemDTO);
        if (questionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestionItemDTO result = questionItemService.update(questionItemDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, questionItemDTO.getId().toString()));
    }

    /**
     * {@code GET  /question-items} : get all the questionItems.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of questionItems in body.
     */
    @Get("/question-items")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<QuestionItemDTO>> getAllQuestionItems(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of QuestionItems");
        Page<QuestionItemDTO> page = questionItemService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /question-items/:id} : get the "id" questionItem.
     *
     * @param id the id of the questionItemDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the questionItemDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/question-items/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<QuestionItemDTO> getQuestionItem(@PathVariable Long id) {
        log.debug("REST request to get QuestionItem : {}", id);
        
        return questionItemService.findOne(id);
    }

    /**
     * {@code DELETE  /question-items/:id} : delete the "id" questionItem.
     *
     * @param id the id of the questionItemDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/question-items/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteQuestionItem(@PathVariable Long id) {
        log.debug("REST request to delete QuestionItem : {}", id);
        questionItemService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
