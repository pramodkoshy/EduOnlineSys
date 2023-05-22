package education.web.rest;

import education.service.CourseLearningObjectsService;
import education.web.rest.errors.BadRequestAlertException;
import education.service.dto.CourseLearningObjectsDTO;

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
 * REST controller for managing {@link education.domain.CourseLearningObjects}.
 */
@Controller("/api")
public class CourseLearningObjectsResource {

    private final Logger log = LoggerFactory.getLogger(CourseLearningObjectsResource.class);

    private static final String ENTITY_NAME = "courseLearningObjects";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseLearningObjectsService courseLearningObjectsService;

    public CourseLearningObjectsResource(CourseLearningObjectsService courseLearningObjectsService) {
        this.courseLearningObjectsService = courseLearningObjectsService;
    }

    /**
     * {@code POST  /course-learning-objects} : Create a new courseLearningObjects.
     *
     * @param courseLearningObjectsDTO the courseLearningObjectsDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new courseLearningObjectsDTO, or with status {@code 400 (Bad Request)} if the courseLearningObjects has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/course-learning-objects")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseLearningObjectsDTO> createCourseLearningObjects(@Body CourseLearningObjectsDTO courseLearningObjectsDTO) throws URISyntaxException {
        log.debug("REST request to save CourseLearningObjects : {}", courseLearningObjectsDTO);
        if (courseLearningObjectsDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseLearningObjects cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseLearningObjectsDTO result = courseLearningObjectsService.save(courseLearningObjectsDTO);
        URI location = new URI("/api/course-learning-objects/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /course-learning-objects} : Updates an existing courseLearningObjects.
     *
     * @param courseLearningObjectsDTO the courseLearningObjectsDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated courseLearningObjectsDTO,
     * or with status {@code 400 (Bad Request)} if the courseLearningObjectsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseLearningObjectsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/course-learning-objects")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseLearningObjectsDTO> updateCourseLearningObjects(@Body CourseLearningObjectsDTO courseLearningObjectsDTO) throws URISyntaxException {
        log.debug("REST request to update CourseLearningObjects : {}", courseLearningObjectsDTO);
        if (courseLearningObjectsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseLearningObjectsDTO result = courseLearningObjectsService.update(courseLearningObjectsDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, courseLearningObjectsDTO.getId().toString()));
    }

    /**
     * {@code GET  /course-learning-objects} : get all the courseLearningObjects.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of courseLearningObjects in body.
     */
    @Get("/course-learning-objects")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<CourseLearningObjectsDTO>> getAllCourseLearningObjects(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of CourseLearningObjects");
        Page<CourseLearningObjectsDTO> page = courseLearningObjectsService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /course-learning-objects/:id} : get the "id" courseLearningObjects.
     *
     * @param id the id of the courseLearningObjectsDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the courseLearningObjectsDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/course-learning-objects/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<CourseLearningObjectsDTO> getCourseLearningObjects(@PathVariable Long id) {
        log.debug("REST request to get CourseLearningObjects : {}", id);
        
        return courseLearningObjectsService.findOne(id);
    }

    /**
     * {@code DELETE  /course-learning-objects/:id} : delete the "id" courseLearningObjects.
     *
     * @param id the id of the courseLearningObjectsDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/course-learning-objects/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteCourseLearningObjects(@PathVariable Long id) {
        log.debug("REST request to delete CourseLearningObjects : {}", id);
        courseLearningObjectsService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
