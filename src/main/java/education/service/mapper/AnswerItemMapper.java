package education.service.mapper;


import education.domain.*;
import education.service.dto.AnswerItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnswerItem} and its DTO {@link AnswerItemDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface AnswerItemMapper extends EntityMapper<AnswerItemDTO, AnswerItem> {



    default AnswerItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        AnswerItem answerItem = new AnswerItem();
        answerItem.setId(id);
        return answerItem;
    }
}
