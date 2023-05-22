package education.service.mapper;


import education.domain.*;
import education.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {


    @Mapping(target = "courseModules", ignore = true)
    @Mapping(target = "removeCourseModule", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "removeStudents", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
