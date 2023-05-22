package education.service.impl;

import education.service.QuestionItemService;
import education.domain.QuestionItem;
import education.repository.QuestionItemRepository;
import education.service.dto.QuestionItemDTO;
import education.service.mapper.QuestionItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.Optional;

/**
 * Service Implementation for managing {@link QuestionItem}.
 */
@Singleton
@Transactional
public class QuestionItemServiceImpl implements QuestionItemService {

    private final Logger log = LoggerFactory.getLogger(QuestionItemServiceImpl.class);

    private final QuestionItemRepository questionItemRepository;

    private final QuestionItemMapper questionItemMapper;

    public QuestionItemServiceImpl(QuestionItemRepository questionItemRepository, QuestionItemMapper questionItemMapper) {
        this.questionItemRepository = questionItemRepository;
        this.questionItemMapper = questionItemMapper;
    }

    /**
     * Save a questionItem.
     *
     * @param questionItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public QuestionItemDTO save(QuestionItemDTO questionItemDTO) {
        log.debug("Request to save QuestionItem : {}", questionItemDTO);
        QuestionItem questionItem = questionItemMapper.toEntity(questionItemDTO);
        questionItem = questionItemRepository.save(questionItem);
        return questionItemMapper.toDto(questionItem);
    }

    /**
     * Update a questionItem.
     *
     * @param questionItemDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public QuestionItemDTO update(QuestionItemDTO questionItemDTO) {
        log.debug("Request to update QuestionItem : {}", questionItemDTO);
        QuestionItem questionItem = questionItemMapper.toEntity(questionItemDTO);
        questionItem = questionItemRepository.update(questionItem);
        return questionItemMapper.toDto(questionItem);
    }

    /**
     * Get all the questionItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public Page<QuestionItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionItems");
        return questionItemRepository.findAll(pageable)
            .map(questionItemMapper::toDto);
    }


    /**
     * Get one questionItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<QuestionItemDTO> findOne(Long id) {
        log.debug("Request to get QuestionItem : {}", id);
        return questionItemRepository.findById(id)
            .map(questionItemMapper::toDto);
    }

    /**
     * Delete the questionItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionItem : {}", id);
        questionItemRepository.deleteById(id);
    }
}
