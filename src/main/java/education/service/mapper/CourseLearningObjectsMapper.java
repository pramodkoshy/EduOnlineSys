package education.service.mapper;


import education.domain.*;
import education.service.dto.CourseLearningObjectsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourseLearningObjects} and its DTO {@link CourseLearningObjectsDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {CourseUnitMapper.class})
public interface CourseLearningObjectsMapper extends EntityMapper<CourseLearningObjectsDTO, CourseLearningObjects> {

    @Mapping(source = "courseUnit.id", target = "courseUnitId")
    @Mapping(source = "courseUnit.name", target = "courseUnitName")
    CourseLearningObjectsDTO toDto(CourseLearningObjects courseLearningObjects);

    @Mapping(source = "courseUnitId", target = "courseUnit")
    CourseLearningObjects toEntity(CourseLearningObjectsDTO courseLearningObjectsDTO);

    default CourseLearningObjects fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseLearningObjects courseLearningObjects = new CourseLearningObjects();
        courseLearningObjects.setId(id);
        return courseLearningObjects;
    }
}
