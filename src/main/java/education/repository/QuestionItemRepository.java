package education.repository;

import education.domain.QuestionItem;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the QuestionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionItemRepository extends JpaRepository<QuestionItem, Long> {

}
