package education.web.rest;

import education.service.ExamRegistrationService;
import education.web.rest.errors.BadRequestAlertException;
import education.service.dto.ExamRegistrationDTO;

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
 * REST controller for managing {@link education.domain.ExamRegistration}.
 */
@Controller("/api")
public class ExamRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(ExamRegistrationResource.class);

    private static final String ENTITY_NAME = "examRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamRegistrationService examRegistrationService;

    public ExamRegistrationResource(ExamRegistrationService examRegistrationService) {
        this.examRegistrationService = examRegistrationService;
    }

    /**
     * {@code POST  /exam-registrations} : Create a new examRegistration.
     *
     * @param examRegistrationDTO the examRegistrationDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new examRegistrationDTO, or with status {@code 400 (Bad Request)} if the examRegistration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/exam-registrations")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<ExamRegistrationDTO> createExamRegistration(@Body ExamRegistrationDTO examRegistrationDTO) throws URISyntaxException {
        log.debug("REST request to save ExamRegistration : {}", examRegistrationDTO);
        if (examRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new examRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamRegistrationDTO result = examRegistrationService.save(examRegistrationDTO);
        URI location = new URI("/api/exam-registrations/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /exam-registrations} : Updates an existing examRegistration.
     *
     * @param examRegistrationDTO the examRegistrationDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated examRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the examRegistrationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/exam-registrations")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<ExamRegistrationDTO> updateExamRegistration(@Body ExamRegistrationDTO examRegistrationDTO) throws URISyntaxException {
        log.debug("REST request to update ExamRegistration : {}", examRegistrationDTO);
        if (examRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExamRegistrationDTO result = examRegistrationService.update(examRegistrationDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, examRegistrationDTO.getId().toString()));
    }

    /**
     * {@code GET  /exam-registrations} : get all the examRegistrations.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of examRegistrations in body.
     */
    @Get("/exam-registrations")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<ExamRegistrationDTO>> getAllExamRegistrations(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of ExamRegistrations");
        Page<ExamRegistrationDTO> page = examRegistrationService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /exam-registrations/:id} : get the "id" examRegistration.
     *
     * @param id the id of the examRegistrationDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the examRegistrationDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/exam-registrations/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<ExamRegistrationDTO> getExamRegistration(@PathVariable Long id) {
        log.debug("REST request to get ExamRegistration : {}", id);
        
        return examRegistrationService.findOne(id);
    }

    /**
     * {@code DELETE  /exam-registrations/:id} : delete the "id" examRegistration.
     *
     * @param id the id of the examRegistrationDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/exam-registrations/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteExamRegistration(@PathVariable Long id) {
        log.debug("REST request to delete ExamRegistration : {}", id);
        examRegistrationService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
