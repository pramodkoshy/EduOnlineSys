package education.service.impl;

import education.service.CourseEnrolmentService;
import education.domain.CourseEnrolment;
import education.repository.CourseEnrolmentRepository;
import education.service.dto.CourseEnrolmentDTO;
import education.service.mapper.CourseEnrolmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseEnrolment}.
 */
@Singleton
@Transactional
public class CourseEnrolmentServiceImpl implements CourseEnrolmentService {

    private final Logger log = LoggerFactory.getLogger(CourseEnrolmentServiceImpl.class);

    private final CourseEnrolmentRepository courseEnrolmentRepository;

    private final CourseEnrolmentMapper courseEnrolmentMapper;

    public CourseEnrolmentServiceImpl(CourseEnrolmentRepository courseEnrolmentRepository, CourseEnrolmentMapper courseEnrolmentMapper) {
        this.courseEnrolmentRepository = courseEnrolmentRepository;
        this.courseEnrolmentMapper = courseEnrolmentMapper;
    }

    /**
     * Save a courseEnrolment.
     *
     * @param courseEnrolmentDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseEnrolmentDTO save(CourseEnrolmentDTO courseEnrolmentDTO) {
        log.debug("Request to save CourseEnrolment : {}", courseEnrolmentDTO);
        CourseEnrolment courseEnrolment = courseEnrolmentMapper.toEntity(courseEnrolmentDTO);
        courseEnrolment = courseEnrolmentRepository.save(courseEnrolment);
        return courseEnrolmentMapper.toDto(courseEnrolment);
    }

    /**
     * Update a courseEnrolment.
     *
     * @param courseEnrolmentDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public CourseEnrolmentDTO update(CourseEnrolmentDTO courseEnrolmentDTO) {
        log.debug("Request to update CourseEnrolment : {}", courseEnrolmentDTO);
        CourseEnrolment courseEnrolment = courseEnrolmentMapper.toEntity(courseEnrolmentDTO);
        courseEnrolment = courseEnrolmentRepository.update(courseEnrolment);
        return courseEnrolmentMapper.toDto(courseEnrolment);
    }

    /**
     * Get all the courseEnrolments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public Page<CourseEnrolmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseEnrolments");
        return courseEnrolmentRepository.findAll(pageable)
            .map(courseEnrolmentMapper::toDto);
    }


    /**
     * Get one courseEnrolment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<CourseEnrolmentDTO> findOne(Long id) {
        log.debug("Request to get CourseEnrolment : {}", id);
        return courseEnrolmentRepository.findById(id)
            .map(courseEnrolmentMapper::toDto);
    }

    /**
     * Delete the courseEnrolment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseEnrolment : {}", id);
        courseEnrolmentRepository.deleteById(id);
    }
}
