package education.web.rest;

import education.service.CourseUnitService;
import education.web.rest.errors.BadRequestAlertException;
import education.service.dto.CourseUnitDTO;

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
 * REST controller for managing {@link education.domain.CourseUnit}.
 */
@Controller("/api")
public class CourseUnitResource {

    private final Logger log = LoggerFactory.getLogger(CourseUnitResource.class);

    private static final String ENTITY_NAME = "courseUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseUnitService courseUnitService;

    public CourseUnitResource(CourseUnitService courseUnitService) {
        this.courseUnitService = courseUnitService;
    }

    /**
     * {@code POST  /course-units} : Create a new courseUnit.
     *
     * @param courseUnitDTO the courseUnitDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new courseUnitDTO, or with status {@code 400 (Bad Request)} if the courseUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/course-units")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseUnitDTO> createCourseUnit(@Body CourseUnitDTO courseUnitDTO) throws URISyntaxException {
        log.debug("REST request to save CourseUnit : {}", courseUnitDTO);
        if (courseUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseUnitDTO result = courseUnitService.save(courseUnitDTO);
        URI location = new URI("/api/course-units/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /course-units} : Updates an existing courseUnit.
     *
     * @param courseUnitDTO the courseUnitDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated courseUnitDTO,
     * or with status {@code 400 (Bad Request)} if the courseUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/course-units")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseUnitDTO> updateCourseUnit(@Body CourseUnitDTO courseUnitDTO) throws URISyntaxException {
        log.debug("REST request to update CourseUnit : {}", courseUnitDTO);
        if (courseUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseUnitDTO result = courseUnitService.update(courseUnitDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, courseUnitDTO.getId().toString()));
    }

    /**
     * {@code GET  /course-units} : get all the courseUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of courseUnits in body.
     */
    @Get("/course-units")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<CourseUnitDTO>> getAllCourseUnits(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of CourseUnits");
        Page<CourseUnitDTO> page = courseUnitService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /course-units/:id} : get the "id" courseUnit.
     *
     * @param id the id of the courseUnitDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the courseUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/course-units/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<CourseUnitDTO> getCourseUnit(@PathVariable Long id) {
        log.debug("REST request to get CourseUnit : {}", id);
        
        return courseUnitService.findOne(id);
    }

    /**
     * {@code DELETE  /course-units/:id} : delete the "id" courseUnit.
     *
     * @param id the id of the courseUnitDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/course-units/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteCourseUnit(@PathVariable Long id) {
        log.debug("REST request to delete CourseUnit : {}", id);
        courseUnitService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
