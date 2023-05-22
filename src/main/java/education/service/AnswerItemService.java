package education.service;

import education.service.dto.AnswerItemDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.AnswerItem}.
 */
public interface AnswerItemService {

    /**
     * Save a answerItem.
     *
     * @param answerItemDTO the entity to save.
     * @return the persisted entity.
     */
    AnswerItemDTO save(AnswerItemDTO answerItemDTO);

    /**
     * Update a answerItem.
     *
     * @param answerItemDTO the entity to save.
     * @return the persisted entity.
     */
    AnswerItemDTO update(AnswerItemDTO answerItemDTO);

    /**
     * Get all the answerItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnswerItemDTO> findAll(Pageable pageable);


    /**
     * Get the "id" answerItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnswerItemDTO> findOne(Long id);

    /**
     * Delete the "id" answerItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
