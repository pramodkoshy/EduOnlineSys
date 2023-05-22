package education.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class ExamRegistrationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamRegistrationDTO.class);
        ExamRegistrationDTO examRegistrationDTO1 = new ExamRegistrationDTO();
        examRegistrationDTO1.setId(1L);
        ExamRegistrationDTO examRegistrationDTO2 = new ExamRegistrationDTO();
        assertThat(examRegistrationDTO1).isNotEqualTo(examRegistrationDTO2);
        examRegistrationDTO2.setId(examRegistrationDTO1.getId());
        assertThat(examRegistrationDTO1).isEqualTo(examRegistrationDTO2);
        examRegistrationDTO2.setId(2L);
        assertThat(examRegistrationDTO1).isNotEqualTo(examRegistrationDTO2);
        examRegistrationDTO1.setId(null);
        assertThat(examRegistrationDTO1).isNotEqualTo(examRegistrationDTO2);
    }
}
