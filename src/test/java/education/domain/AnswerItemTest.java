package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class AnswerItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerItem.class);
        AnswerItem answerItem1 = new AnswerItem();
        answerItem1.setId(1L);
        AnswerItem answerItem2 = new AnswerItem();
        answerItem2.setId(answerItem1.getId());
        assertThat(answerItem1).isEqualTo(answerItem2);
        answerItem2.setId(2L);
        assertThat(answerItem1).isNotEqualTo(answerItem2);
        answerItem1.setId(null);
        assertThat(answerItem1).isNotEqualTo(answerItem2);
    }
}
