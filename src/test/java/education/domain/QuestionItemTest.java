package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class QuestionItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionItem.class);
        QuestionItem questionItem1 = new QuestionItem();
        questionItem1.setId(1L);
        QuestionItem questionItem2 = new QuestionItem();
        questionItem2.setId(questionItem1.getId());
        assertThat(questionItem1).isEqualTo(questionItem2);
        questionItem2.setId(2L);
        assertThat(questionItem1).isNotEqualTo(questionItem2);
        questionItem1.setId(null);
        assertThat(questionItem1).isNotEqualTo(questionItem2);
    }
}
