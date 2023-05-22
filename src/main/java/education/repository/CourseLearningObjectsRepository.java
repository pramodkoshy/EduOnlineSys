package education.repository;

import education.domain.CourseLearningObjects;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the CourseLearningObjects entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseLearningObjectsRepository extends JpaRepository<CourseLearningObjects, Long> {

}
