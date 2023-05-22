package education.service.impl;

import education.service.ExamRegistrationService;
import education.domain.ExamRegistration;
import education.repository.ExamRegistrationRepository;
import education.service.dto.ExamRegistrationDTO;
import education.service.mapper.ExamRegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExamRegistration}.
 */
@Singleton
@Transactional
public class ExamRegistrationServiceImpl implements ExamRegistrationService {

    private final Logger log = LoggerFactory.getLogger(ExamRegistrationServiceImpl.class);

    private final ExamRegistrationRepository examRegistrationRepository;

    private final ExamRegistrationMapper examRegistrationMapper;

    public ExamRegistrationServiceImpl(ExamRegistrationRepository examRegistrationRepository, ExamRegistrationMapper examRegistrationMapper) {
        this.examRegistrationRepository = examRegistrationRepository;
        this.examRegistrationMapper = examRegistrationMapper;
    }

    /**
     * Save a examRegistration.
     *
     * @param examRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ExamRegistrationDTO save(ExamRegistrationDTO examRegistrationDTO) {
        log.debug("Request to save ExamRegistration : {}", examRegistrationDTO);
        ExamRegistration examRegistration = examRegistrationMapper.toEntity(examRegistrationDTO);
        examRegistration = examRegistrationRepository.save(examRegistration);
        return examRegistrationMapper.toDto(examRegistration);
    }

    /**
     * Update a examRegistration.
     *
     * @param examRegistrationDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public ExamRegistrationDTO update(ExamRegistrationDTO examRegistrationDTO) {
        log.debug("Request to update ExamRegistration : {}", examRegistrationDTO);
        ExamRegistration examRegistration = examRegistrationMapper.toEntity(examRegistrationDTO);
        examRegistration = examRegistrationRepository.update(examRegistration);
        return examRegistrationMapper.toDto(examRegistration);
    }

    /**
     * Get all the examRegistrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public Page<ExamRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExamRegistrations");
        return examRegistrationRepository.findAll(pageable)
            .map(examRegistrationMapper::toDto);
    }


    /**
     * Get one examRegistration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<ExamRegistrationDTO> findOne(Long id) {
        log.debug("Request to get ExamRegistration : {}", id);
        return examRegistrationRepository.findById(id)
            .map(examRegistrationMapper::toDto);
    }

    /**
     * Delete the examRegistration by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExamRegistration : {}", id);
        examRegistrationRepository.deleteById(id);
    }
}
