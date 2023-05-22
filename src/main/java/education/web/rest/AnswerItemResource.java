package education.web.rest;

import education.service.AnswerItemService;
import education.web.rest.errors.BadRequestAlertException;
import education.service.dto.AnswerItemDTO;

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
 * REST controller for managing {@link education.domain.AnswerItem}.
 */
@Controller("/api")
public class AnswerItemResource {

    private final Logger log = LoggerFactory.getLogger(AnswerItemResource.class);

    private static final String ENTITY_NAME = "answerItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerItemService answerItemService;

    public AnswerItemResource(AnswerItemService answerItemService) {
        this.answerItemService = answerItemService;
    }

    /**
     * {@code POST  /answer-items} : Create a new answerItem.
     *
     * @param answerItemDTO the answerItemDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new answerItemDTO, or with status {@code 400 (Bad Request)} if the answerItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/answer-items")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<AnswerItemDTO> createAnswerItem(@Body AnswerItemDTO answerItemDTO) throws URISyntaxException {
        log.debug("REST request to save AnswerItem : {}", answerItemDTO);
        if (answerItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new answerItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnswerItemDTO result = answerItemService.save(answerItemDTO);
        URI location = new URI("/api/answer-items/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /answer-items} : Updates an existing answerItem.
     *
     * @param answerItemDTO the answerItemDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated answerItemDTO,
     * or with status {@code 400 (Bad Request)} if the answerItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/answer-items")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<AnswerItemDTO> updateAnswerItem(@Body AnswerItemDTO answerItemDTO) throws URISyntaxException {
        log.debug("REST request to update AnswerItem : {}", answerItemDTO);
        if (answerItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnswerItemDTO result = answerItemService.update(answerItemDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, answerItemDTO.getId().toString()));
    }

    /**
     * {@code GET  /answer-items} : get all the answerItems.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of answerItems in body.
     */
    @Get("/answer-items")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<AnswerItemDTO>> getAllAnswerItems(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of AnswerItems");
        Page<AnswerItemDTO> page = answerItemService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /answer-items/:id} : get the "id" answerItem.
     *
     * @param id the id of the answerItemDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the answerItemDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/answer-items/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<AnswerItemDTO> getAnswerItem(@PathVariable Long id) {
        log.debug("REST request to get AnswerItem : {}", id);
        
        return answerItemService.findOne(id);
    }

    /**
     * {@code DELETE  /answer-items/:id} : delete the "id" answerItem.
     *
     * @param id the id of the answerItemDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/answer-items/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteAnswerItem(@PathVariable Long id) {
        log.debug("REST request to delete AnswerItem : {}", id);
        answerItemService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
