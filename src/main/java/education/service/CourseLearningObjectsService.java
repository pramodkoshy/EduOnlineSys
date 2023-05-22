package education.service;

import education.service.dto.CourseLearningObjectsDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.CourseLearningObjects}.
 */
public interface CourseLearningObjectsService {

    /**
     * Save a courseLearningObjects.
     *
     * @param courseLearningObjectsDTO the entity to save.
     * @return the persisted entity.
     */
    CourseLearningObjectsDTO save(CourseLearningObjectsDTO courseLearningObjectsDTO);

    /**
     * Update a courseLearningObjects.
     *
     * @param courseLearningObjectsDTO the entity to save.
     * @return the persisted entity.
     */
    CourseLearningObjectsDTO update(CourseLearningObjectsDTO courseLearningObjectsDTO);

    /**
     * Get all the courseLearningObjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseLearningObjectsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" courseLearningObjects.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseLearningObjectsDTO> findOne(Long id);

    /**
     * Delete the "id" courseLearningObjects.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
