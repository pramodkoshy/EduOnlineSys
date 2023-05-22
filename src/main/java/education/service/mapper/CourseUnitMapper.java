package education.service.mapper;


import education.domain.*;
import education.service.dto.CourseUnitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourseUnit} and its DTO {@link CourseUnitDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {CourseModuleMapper.class})
public interface CourseUnitMapper extends EntityMapper<CourseUnitDTO, CourseUnit> {

    @Mapping(source = "courseModule.id", target = "courseModuleId")
    @Mapping(source = "courseModule.name", target = "courseModuleName")
    CourseUnitDTO toDto(CourseUnit courseUnit);

    @Mapping(target = "courseLearningObjects", ignore = true)
    @Mapping(target = "removeCourseLearningObjects", ignore = true)
    @Mapping(source = "courseModuleId", target = "courseModule")
    CourseUnit toEntity(CourseUnitDTO courseUnitDTO);

    default CourseUnit fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseUnit courseUnit = new CourseUnit();
        courseUnit.setId(id);
        return courseUnit;
    }
}
