package education.service.impl;

import education.service.CourseUnitService;
import education.domain.CourseUnit;
import education.repository.CourseUnitRepository;
import education.service.dto.CourseUnitDTO;
import education.service.mapper.CourseUnitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseUnit}.
 */
@Singleton
@Transactional
public class CourseUnitServiceImpl implements CourseUnitService {

    private final Logger log = LoggerFactory.getLogger(CourseUnitServiceImpl.class);

    private final CourseUnitRepository courseUnitRepository;

    private final CourseUnitMapper courseUnitMapper;

    public CourseUnitServiceImpl(CourseUnitRepository courseUnitRepository, CourseUnitMapper courseUnitMapper) {
        this.courseUnitRepository = courseUnitRepository;
        this.courseUnitMapper = courseUnitMapper;
    }

    /**
     * Save a courseUnit.
     *
     * @param courseUnitDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseUnitDTO save(CourseUnitDTO courseUnitDTO) {
        log.debug("Request to save CourseUnit : {}", courseUnitDTO);
        CourseUnit courseUnit = courseUnitMapper.toEntity(courseUnitDTO);
        courseUnit = courseUnitRepository.save(courseUnit);
        return courseUnitMapper.toDto(courseUnit);
    }

    /**
     * Update a courseUnit.
     *
     * @param courseUnitDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public CourseUnitDTO update(CourseUnitDTO courseUnitDTO) {
        log.debug("Request to update CourseUnit : {}", courseUnitDTO);
        CourseUnit courseUnit = courseUnitMapper.toEntity(courseUnitDTO);
        courseUnit = courseUnitRepository.update(courseUnit);
        return courseUnitMapper.toDto(courseUnit);
    }

    /**
     * Get all the courseUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public Page<CourseUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseUnits");
        return courseUnitRepository.findAll(pageable)
            .map(courseUnitMapper::toDto);
    }


    /**
     * Get one courseUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<CourseUnitDTO> findOne(Long id) {
        log.debug("Request to get CourseUnit : {}", id);
        return courseUnitRepository.findById(id)
            .map(courseUnitMapper::toDto);
    }

    /**
     * Delete the courseUnit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseUnit : {}", id);
        courseUnitRepository.deleteById(id);
    }
}
