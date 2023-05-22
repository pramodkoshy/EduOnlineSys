package education.repository;

import education.domain.CourseEnrolment;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the CourseEnrolment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseEnrolmentRepository extends JpaRepository<CourseEnrolment, Long> {

}
