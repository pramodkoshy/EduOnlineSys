package education.repository;

import education.domain.AnswerItem;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the AnswerItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerItemRepository extends JpaRepository<AnswerItem, Long> {

}
