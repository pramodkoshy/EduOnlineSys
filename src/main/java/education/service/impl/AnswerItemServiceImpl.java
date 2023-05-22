package education.service.impl;

import education.service.AnswerItemService;
import education.domain.AnswerItem;
import education.repository.AnswerItemRepository;
import education.service.dto.AnswerItemDTO;
import education.service.mapper.AnswerItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AnswerItem}.
 */
@Singleton
@Transactional
public class AnswerItemServiceImpl implements AnswerItemService {

    private final Logger log = LoggerFactory.getLogger(AnswerItemServiceImpl.class);

    private final AnswerItemRepository answerItemRepository;

    private final AnswerItemMapper answerItemMapper;

    public AnswerItemServiceImpl(AnswerItemRepository answerItemRepository, AnswerItemMapper answerItemMapper) {
        this.answerItemRepository = answerItemRepository;
        this.answerItemMapper = answerItemMapper;
    }

    /**
     * Save a answerItem.
     *
     * @param answerItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AnswerItemDTO save(AnswerItemDTO answerItemDTO) {
        log.debug("Request to save AnswerItem : {}", answerItemDTO);
        AnswerItem answerItem = answerItemMapper.toEntity(answerItemDTO);
        answerItem = answerItemRepository.save(answerItem);
        return answerItemMapper.toDto(answerItem);
    }

    /**
     * Update a answerItem.
     *
     * @param answerItemDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public AnswerItemDTO update(AnswerItemDTO answerItemDTO) {
        log.debug("Request to update AnswerItem : {}", answerItemDTO);
        AnswerItem answerItem = answerItemMapper.toEntity(answerItemDTO);
        answerItem = answerItemRepository.update(answerItem);
        return answerItemMapper.toDto(answerItem);
    }

    /**
     * Get all the answerItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public Page<AnswerItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnswerItems");
        return answerItemRepository.findAll(pageable)
            .map(answerItemMapper::toDto);
    }


    /**
     * Get one answerItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<AnswerItemDTO> findOne(Long id) {
        log.debug("Request to get AnswerItem : {}", id);
        return answerItemRepository.findById(id)
            .map(answerItemMapper::toDto);
    }

    /**
     * Delete the answerItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnswerItem : {}", id);
        answerItemRepository.deleteById(id);
    }
}
