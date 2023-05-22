package education.service;

import education.service.dto.ExamRegistrationDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.ExamRegistration}.
 */
public interface ExamRegistrationService {

    /**
     * Save a examRegistration.
     *
     * @param examRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    ExamRegistrationDTO save(ExamRegistrationDTO examRegistrationDTO);

    /**
     * Update a examRegistration.
     *
     * @param examRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    ExamRegistrationDTO update(ExamRegistrationDTO examRegistrationDTO);

    /**
     * Get all the examRegistrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamRegistrationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" examRegistration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamRegistrationDTO> findOne(Long id);

    /**
     * Delete the "id" examRegistration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
