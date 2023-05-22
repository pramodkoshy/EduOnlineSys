package education.repository;

import education.domain.CourseModule;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the CourseModule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

}
