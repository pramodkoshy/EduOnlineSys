package education.service.mapper;


import education.domain.*;
import education.service.dto.QuestionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {ExamMapper.class})
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {

    @Mapping(source = "exam.id", target = "examId")
    @Mapping(source = "exam.name", target = "examName")
    QuestionDTO toDto(Question question);

    @Mapping(target = "questionItems", ignore = true)
    @Mapping(target = "removeQuestionItem", ignore = true)
    @Mapping(source = "examId", target = "exam")
    Question toEntity(QuestionDTO questionDTO);

    default Question fromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
