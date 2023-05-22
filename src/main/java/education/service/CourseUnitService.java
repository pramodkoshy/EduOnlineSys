package education.service;

import education.service.dto.CourseUnitDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.CourseUnit}.
 */
public interface CourseUnitService {

    /**
     * Save a courseUnit.
     *
     * @param courseUnitDTO the entity to save.
     * @return the persisted entity.
     */
    CourseUnitDTO save(CourseUnitDTO courseUnitDTO);

    /**
     * Update a courseUnit.
     *
     * @param courseUnitDTO the entity to save.
     * @return the persisted entity.
     */
    CourseUnitDTO update(CourseUnitDTO courseUnitDTO);

    /**
     * Get all the courseUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseUnitDTO> findAll(Pageable pageable);


    /**
     * Get the "id" courseUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseUnitDTO> findOne(Long id);

    /**
     * Delete the "id" courseUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
