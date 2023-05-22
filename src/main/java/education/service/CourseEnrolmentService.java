package education.service;

import education.service.dto.CourseEnrolmentDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.CourseEnrolment}.
 */
public interface CourseEnrolmentService {

    /**
     * Save a courseEnrolment.
     *
     * @param courseEnrolmentDTO the entity to save.
     * @return the persisted entity.
     */
    CourseEnrolmentDTO save(CourseEnrolmentDTO courseEnrolmentDTO);

    /**
     * Update a courseEnrolment.
     *
     * @param courseEnrolmentDTO the entity to save.
     * @return the persisted entity.
     */
    CourseEnrolmentDTO update(CourseEnrolmentDTO courseEnrolmentDTO);

    /**
     * Get all the courseEnrolments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseEnrolmentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" courseEnrolment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseEnrolmentDTO> findOne(Long id);

    /**
     * Delete the "id" courseEnrolment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
