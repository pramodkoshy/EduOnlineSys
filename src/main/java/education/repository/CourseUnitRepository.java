package education.repository;

import education.domain.CourseUnit;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the CourseUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseUnitRepository extends JpaRepository<CourseUnit, Long> {

}
