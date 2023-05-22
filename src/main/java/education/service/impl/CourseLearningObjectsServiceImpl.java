package education.service.impl;

import education.service.CourseLearningObjectsService;
import education.domain.CourseLearningObjects;
import education.repository.CourseLearningObjectsRepository;
import education.service.dto.CourseLearningObjectsDTO;
import education.service.mapper.CourseLearningObjectsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseLearningObjects}.
 */
@Singleton
@Transactional
public class CourseLearningObjectsServiceImpl implements CourseLearningObjectsService {

    private final Logger log = LoggerFactory.getLogger(CourseLearningObjectsServiceImpl.class);

    private final CourseLearningObjectsRepository courseLearningObjectsRepository;

    private final CourseLearningObjectsMapper courseLearningObjectsMapper;

    public CourseLearningObjectsServiceImpl(CourseLearningObjectsRepository courseLearningObjectsRepository, CourseLearningObjectsMapper courseLearningObjectsMapper) {
        this.courseLearningObjectsRepository = courseLearningObjectsRepository;
        this.courseLearningObjectsMapper = courseLearningObjectsMapper;
    }

    /**
     * Save a courseLearningObjects.
     *
     * @param courseLearningObjectsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseLearningObjectsDTO save(CourseLearningObjectsDTO courseLearningObjectsDTO) {
        log.debug("Request to save CourseLearningObjects : {}", courseLearningObjectsDTO);
        CourseLearningObjects courseLearningObjects = courseLearningObjectsMapper.toEntity(courseLearningObjectsDTO);
        courseLearningObjects = courseLearningObjectsRepository.save(courseLearningObjects);
        return courseLearningObjectsMapper.toDto(courseLearningObjects);
    }

    /**
     * Update a courseLearningObjects.
     *
     * @param courseLearningObjectsDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public CourseLearningObjectsDTO update(CourseLearningObjectsDTO courseLearningObjectsDTO) {
        log.debug("Request to update CourseLearningObjects : {}", courseLearningObjectsDTO);
        CourseLearningObjects courseLearningObjects = courseLearningObjectsMapper.toEntity(courseLearningObjectsDTO);
        courseLearningObjects = courseLearningObjectsRepository.update(courseLearningObjects);
        return courseLearningObjectsMapper.toDto(courseLearningObjects);
    }

    /**
     * Get all the courseLearningObjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public Page<CourseLearningObjectsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseLearningObjects");
        return courseLearningObjectsRepository.findAll(pageable)
            .map(courseLearningObjectsMapper::toDto);
    }


    /**
     * Get one courseLearningObjects by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<CourseLearningObjectsDTO> findOne(Long id) {
        log.debug("Request to get CourseLearningObjects : {}", id);
        return courseLearningObjectsRepository.findById(id)
            .map(courseLearningObjectsMapper::toDto);
    }

    /**
     * Delete the courseLearningObjects by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseLearningObjects : {}", id);
        courseLearningObjectsRepository.deleteById(id);
    }
}
