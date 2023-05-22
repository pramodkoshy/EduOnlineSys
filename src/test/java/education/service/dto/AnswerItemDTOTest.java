package education.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class AnswerItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerItemDTO.class);
        AnswerItemDTO answerItemDTO1 = new AnswerItemDTO();
        answerItemDTO1.setId(1L);
        AnswerItemDTO answerItemDTO2 = new AnswerItemDTO();
        assertThat(answerItemDTO1).isNotEqualTo(answerItemDTO2);
        answerItemDTO2.setId(answerItemDTO1.getId());
        assertThat(answerItemDTO1).isEqualTo(answerItemDTO2);
        answerItemDTO2.setId(2L);
        assertThat(answerItemDTO1).isNotEqualTo(answerItemDTO2);
        answerItemDTO1.setId(null);
        assertThat(answerItemDTO1).isNotEqualTo(answerItemDTO2);
    }
}
