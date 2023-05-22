package education.service;

import education.service.dto.CourseModuleDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.CourseModule}.
 */
public interface CourseModuleService {

    /**
     * Save a courseModule.
     *
     * @param courseModuleDTO the entity to save.
     * @return the persisted entity.
     */
    CourseModuleDTO save(CourseModuleDTO courseModuleDTO);

    /**
     * Update a courseModule.
     *
     * @param courseModuleDTO the entity to save.
     * @return the persisted entity.
     */
    CourseModuleDTO update(CourseModuleDTO courseModuleDTO);

    /**
     * Get all the courseModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseModuleDTO> findAll(Pageable pageable);


    /**
     * Get the "id" courseModule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseModuleDTO> findOne(Long id);

    /**
     * Delete the "id" courseModule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
