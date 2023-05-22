package education.web.rest;

import education.service.CourseModuleService;
import education.web.rest.errors.BadRequestAlertException;
import education.service.dto.CourseModuleDTO;

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
 * REST controller for managing {@link education.domain.CourseModule}.
 */
@Controller("/api")
public class CourseModuleResource {

    private final Logger log = LoggerFactory.getLogger(CourseModuleResource.class);

    private static final String ENTITY_NAME = "courseModule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseModuleService courseModuleService;

    public CourseModuleResource(CourseModuleService courseModuleService) {
        this.courseModuleService = courseModuleService;
    }

    /**
     * {@code POST  /course-modules} : Create a new courseModule.
     *
     * @param courseModuleDTO the courseModuleDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new courseModuleDTO, or with status {@code 400 (Bad Request)} if the courseModule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/course-modules")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseModuleDTO> createCourseModule(@Body CourseModuleDTO courseModuleDTO) throws URISyntaxException {
        log.debug("REST request to save CourseModule : {}", courseModuleDTO);
        if (courseModuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseModule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseModuleDTO result = courseModuleService.save(courseModuleDTO);
        URI location = new URI("/api/course-modules/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /course-modules} : Updates an existing courseModule.
     *
     * @param courseModuleDTO the courseModuleDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated courseModuleDTO,
     * or with status {@code 400 (Bad Request)} if the courseModuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/course-modules")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseModuleDTO> updateCourseModule(@Body CourseModuleDTO courseModuleDTO) throws URISyntaxException {
        log.debug("REST request to update CourseModule : {}", courseModuleDTO);
        if (courseModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseModuleDTO result = courseModuleService.update(courseModuleDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, courseModuleDTO.getId().toString()));
    }

    /**
     * {@code GET  /course-modules} : get all the courseModules.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of courseModules in body.
     */
    @Get("/course-modules")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<CourseModuleDTO>> getAllCourseModules(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of CourseModules");
        Page<CourseModuleDTO> page = courseModuleService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /course-modules/:id} : get the "id" courseModule.
     *
     * @param id the id of the courseModuleDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the courseModuleDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/course-modules/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<CourseModuleDTO> getCourseModule(@PathVariable Long id) {
        log.debug("REST request to get CourseModule : {}", id);
        
        return courseModuleService.findOne(id);
    }

    /**
     * {@code DELETE  /course-modules/:id} : delete the "id" courseModule.
     *
     * @param id the id of the courseModuleDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/course-modules/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteCourseModule(@PathVariable Long id) {
        log.debug("REST request to delete CourseModule : {}", id);
        courseModuleService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
