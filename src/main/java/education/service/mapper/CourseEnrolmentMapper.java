package education.service.mapper;


import education.domain.*;
import education.service.dto.CourseEnrolmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourseEnrolment} and its DTO {@link CourseEnrolmentDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {StudentMapper.class})
public interface CourseEnrolmentMapper extends EntityMapper<CourseEnrolmentDTO, CourseEnrolment> {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    CourseEnrolmentDTO toDto(CourseEnrolment courseEnrolment);

    @Mapping(source = "studentId", target = "student")
    CourseEnrolment toEntity(CourseEnrolmentDTO courseEnrolmentDTO);

    default CourseEnrolment fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseEnrolment courseEnrolment = new CourseEnrolment();
        courseEnrolment.setId(id);
        return courseEnrolment;
    }
}
