package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class ExamRegistrationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamRegistration.class);
        ExamRegistration examRegistration1 = new ExamRegistration();
        examRegistration1.setId(1L);
        ExamRegistration examRegistration2 = new ExamRegistration();
        examRegistration2.setId(examRegistration1.getId());
        assertThat(examRegistration1).isEqualTo(examRegistration2);
        examRegistration2.setId(2L);
        assertThat(examRegistration1).isNotEqualTo(examRegistration2);
        examRegistration1.setId(null);
        assertThat(examRegistration1).isNotEqualTo(examRegistration2);
    }
}
