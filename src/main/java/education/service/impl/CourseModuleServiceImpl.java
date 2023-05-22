package education.service.impl;

import education.service.CourseModuleService;
import education.domain.CourseModule;
import education.repository.CourseModuleRepository;
import education.service.dto.CourseModuleDTO;
import education.service.mapper.CourseModuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseModule}.
 */
@Singleton
@Transactional
public class CourseModuleServiceImpl implements CourseModuleService {

    private final Logger log = LoggerFactory.getLogger(CourseModuleServiceImpl.class);

    private final CourseModuleRepository courseModuleRepository;

    private final CourseModuleMapper courseModuleMapper;

    public CourseModuleServiceImpl(CourseModuleRepository courseModuleRepository, CourseModuleMapper courseModuleMapper) {
        this.courseModuleRepository = courseModuleRepository;
        this.courseModuleMapper = courseModuleMapper;
    }

    /**
     * Save a courseModule.
     *
     * @param courseModuleDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseModuleDTO save(CourseModuleDTO courseModuleDTO) {
        log.debug("Request to save CourseModule : {}", courseModuleDTO);
        CourseModule courseModule = courseModuleMapper.toEntity(courseModuleDTO);
        courseModule = courseModuleRepository.save(courseModule);
        return courseModuleMapper.toDto(courseModule);
    }

    /**
     * Update a courseModule.
     *
     * @param courseModuleDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public CourseModuleDTO update(CourseModuleDTO courseModuleDTO) {
        log.debug("Request to update CourseModule : {}", courseModuleDTO);
        CourseModule courseModule = courseModuleMapper.toEntity(courseModuleDTO);
        courseModule = courseModuleRepository.update(courseModule);
        return courseModuleMapper.toDto(courseModule);
    }

    /**
     * Get all the courseModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public Page<CourseModuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseModules");
        return courseModuleRepository.findAll(pageable)
            .map(courseModuleMapper::toDto);
    }


    /**
     * Get one courseModule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<CourseModuleDTO> findOne(Long id) {
        log.debug("Request to get CourseModule : {}", id);
        return courseModuleRepository.findById(id)
            .map(courseModuleMapper::toDto);
    }

    /**
     * Delete the courseModule by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseModule : {}", id);
        courseModuleRepository.deleteById(id);
    }
}
