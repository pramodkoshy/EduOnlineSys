package education.repository;

import education.domain.Student;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


// TODO what is MN equivalent?
// import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Micronaut Data  repository for the Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    @Query(value = "select distinct student from Student student left join fetch student.courses",
        countQuery = "select count(distinct student) from Student student")
    public Page<Student> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct student from Student student left join fetch student.courses")
    public List<Student> findAllWithEagerRelationships();

    @Query("select student from Student student left join fetch student.courses where student.id =:id")
    public Optional<Student> findOneWithEagerRelationships(Long id);
}
