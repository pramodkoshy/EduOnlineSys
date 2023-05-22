package education.service.mapper;


import education.domain.*;
import education.service.dto.QuestionItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionItem} and its DTO {@link QuestionItemDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {AnswerItemMapper.class, QuestionMapper.class})
public interface QuestionItemMapper extends EntityMapper<QuestionItemDTO, QuestionItem> {

    @Mapping(source = "answerItem.id", target = "answerItemId")
    @Mapping(source = "answerItem.number", target = "answerItemNumber")
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.name", target = "questionName")
    QuestionItemDTO toDto(QuestionItem questionItem);

    @Mapping(source = "answerItemId", target = "answerItem")
    @Mapping(source = "questionId", target = "question")
    QuestionItem toEntity(QuestionItemDTO questionItemDTO);

    default QuestionItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestionItem questionItem = new QuestionItem();
        questionItem.setId(id);
        return questionItem;
    }
}
