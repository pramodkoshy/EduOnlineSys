package education.service;

import education.service.dto.QuestionItemDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.QuestionItem}.
 */
public interface QuestionItemService {

    /**
     * Save a questionItem.
     *
     * @param questionItemDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionItemDTO save(QuestionItemDTO questionItemDTO);

    /**
     * Update a questionItem.
     *
     * @param questionItemDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionItemDTO update(QuestionItemDTO questionItemDTO);

    /**
     * Get all the questionItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionItemDTO> findAll(Pageable pageable);


    /**
     * Get the "id" questionItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionItemDTO> findOne(Long id);

    /**
     * Delete the "id" questionItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
