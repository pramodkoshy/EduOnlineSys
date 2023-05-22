package education.repository;

import education.domain.ExamRegistration;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the ExamRegistration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRegistrationRepository extends JpaRepository<ExamRegistration, Long> {

}
