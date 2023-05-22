package education.service.mapper;


import education.domain.*;
import education.service.dto.CourseModuleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourseModule} and its DTO {@link CourseModuleDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {CourseMapper.class})
public interface CourseModuleMapper extends EntityMapper<CourseModuleDTO, CourseModule> {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    CourseModuleDTO toDto(CourseModule courseModule);

    @Mapping(target = "courseUnits", ignore = true)
    @Mapping(target = "removeCourseUnit", ignore = true)
    @Mapping(source = "courseId", target = "course")
    CourseModule toEntity(CourseModuleDTO courseModuleDTO);

    default CourseModule fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseModule courseModule = new CourseModule();
        courseModule.setId(id);
        return courseModule;
    }
}
