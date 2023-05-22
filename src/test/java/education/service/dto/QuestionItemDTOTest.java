package education.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class QuestionItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionItemDTO.class);
        QuestionItemDTO questionItemDTO1 = new QuestionItemDTO();
        questionItemDTO1.setId(1L);
        QuestionItemDTO questionItemDTO2 = new QuestionItemDTO();
        assertThat(questionItemDTO1).isNotEqualTo(questionItemDTO2);
        questionItemDTO2.setId(questionItemDTO1.getId());
        assertThat(questionItemDTO1).isEqualTo(questionItemDTO2);
        questionItemDTO2.setId(2L);
        assertThat(questionItemDTO1).isNotEqualTo(questionItemDTO2);
        questionItemDTO1.setId(null);
        assertThat(questionItemDTO1).isNotEqualTo(questionItemDTO2);
    }
}
