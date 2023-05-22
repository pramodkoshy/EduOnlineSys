package education.service.mapper;


import education.domain.*;
import education.service.dto.ExamRegistrationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExamRegistration} and its DTO {@link ExamRegistrationDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {TeacherMapper.class})
public interface ExamRegistrationMapper extends EntityMapper<ExamRegistrationDTO, ExamRegistration> {

    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "teacher.name", target = "teacherName")
    ExamRegistrationDTO toDto(ExamRegistration examRegistration);

    @Mapping(source = "teacherId", target = "teacher")
    ExamRegistration toEntity(ExamRegistrationDTO examRegistrationDTO);

    default ExamRegistration fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExamRegistration examRegistration = new ExamRegistration();
        examRegistration.setId(id);
        return examRegistration;
    }
}
