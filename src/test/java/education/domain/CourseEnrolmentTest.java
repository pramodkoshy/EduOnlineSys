package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseEnrolmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseEnrolment.class);
        CourseEnrolment courseEnrolment1 = new CourseEnrolment();
        courseEnrolment1.setId(1L);
        CourseEnrolment courseEnrolment2 = new CourseEnrolment();
        courseEnrolment2.setId(courseEnrolment1.getId());
        assertThat(courseEnrolment1).isEqualTo(courseEnrolment2);
        courseEnrolment2.setId(2L);
        assertThat(courseEnrolment1).isNotEqualTo(courseEnrolment2);
        courseEnrolment1.setId(null);
        assertThat(courseEnrolment1).isNotEqualTo(courseEnrolment2);
    }
}
