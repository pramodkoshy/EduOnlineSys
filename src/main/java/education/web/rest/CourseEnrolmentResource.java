package education.web.rest;

import education.service.CourseEnrolmentService;
import education.web.rest.errors.BadRequestAlertException;
import education.service.dto.CourseEnrolmentDTO;

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
 * REST controller for managing {@link education.domain.CourseEnrolment}.
 */
@Controller("/api")
public class CourseEnrolmentResource {

    private final Logger log = LoggerFactory.getLogger(CourseEnrolmentResource.class);

    private static final String ENTITY_NAME = "courseEnrolment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseEnrolmentService courseEnrolmentService;

    public CourseEnrolmentResource(CourseEnrolmentService courseEnrolmentService) {
        this.courseEnrolmentService = courseEnrolmentService;
    }

    /**
     * {@code POST  /course-enrolments} : Create a new courseEnrolment.
     *
     * @param courseEnrolmentDTO the courseEnrolmentDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new courseEnrolmentDTO, or with status {@code 400 (Bad Request)} if the courseEnrolment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/course-enrolments")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseEnrolmentDTO> createCourseEnrolment(@Body CourseEnrolmentDTO courseEnrolmentDTO) throws URISyntaxException {
        log.debug("REST request to save CourseEnrolment : {}", courseEnrolmentDTO);
        if (courseEnrolmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseEnrolment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseEnrolmentDTO result = courseEnrolmentService.save(courseEnrolmentDTO);
        URI location = new URI("/api/course-enrolments/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /course-enrolments} : Updates an existing courseEnrolment.
     *
     * @param courseEnrolmentDTO the courseEnrolmentDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated courseEnrolmentDTO,
     * or with status {@code 400 (Bad Request)} if the courseEnrolmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseEnrolmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/course-enrolments")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CourseEnrolmentDTO> updateCourseEnrolment(@Body CourseEnrolmentDTO courseEnrolmentDTO) throws URISyntaxException {
        log.debug("REST request to update CourseEnrolment : {}", courseEnrolmentDTO);
        if (courseEnrolmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseEnrolmentDTO result = courseEnrolmentService.update(courseEnrolmentDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, courseEnrolmentDTO.getId().toString()));
    }

    /**
     * {@code GET  /course-enrolments} : get all the courseEnrolments.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of courseEnrolments in body.
     */
    @Get("/course-enrolments")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<CourseEnrolmentDTO>> getAllCourseEnrolments(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of CourseEnrolments");
        Page<CourseEnrolmentDTO> page = courseEnrolmentService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /course-enrolments/:id} : get the "id" courseEnrolment.
     *
     * @param id the id of the courseEnrolmentDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the courseEnrolmentDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/course-enrolments/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<CourseEnrolmentDTO> getCourseEnrolment(@PathVariable Long id) {
        log.debug("REST request to get CourseEnrolment : {}", id);
        
        return courseEnrolmentService.findOne(id);
    }

    /**
     * {@code DELETE  /course-enrolments/:id} : delete the "id" courseEnrolment.
     *
     * @param id the id of the courseEnrolmentDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/course-enrolments/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteCourseEnrolment(@PathVariable Long id) {
        log.debug("REST request to delete CourseEnrolment : {}", id);
        courseEnrolmentService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
