package education.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseEnrolmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseEnrolmentDTO.class);
        CourseEnrolmentDTO courseEnrolmentDTO1 = new CourseEnrolmentDTO();
        courseEnrolmentDTO1.setId(1L);
        CourseEnrolmentDTO courseEnrolmentDTO2 = new CourseEnrolmentDTO();
        assertThat(courseEnrolmentDTO1).isNotEqualTo(courseEnrolmentDTO2);
        courseEnrolmentDTO2.setId(courseEnrolmentDTO1.getId());
        assertThat(courseEnrolmentDTO1).isEqualTo(courseEnrolmentDTO2);
        courseEnrolmentDTO2.setId(2L);
        assertThat(courseEnrolmentDTO1).isNotEqualTo(courseEnrolmentDTO2);
        courseEnrolmentDTO1.setId(null);
        assertThat(courseEnrolmentDTO1).isNotEqualTo(courseEnrolmentDTO2);
    }
}
