package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseUnitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseUnit.class);
        CourseUnit courseUnit1 = new CourseUnit();
        courseUnit1.setId(1L);
        CourseUnit courseUnit2 = new CourseUnit();
        courseUnit2.setId(courseUnit1.getId());
        assertThat(courseUnit1).isEqualTo(courseUnit2);
        courseUnit2.setId(2L);
        assertThat(courseUnit1).isNotEqualTo(courseUnit2);
        courseUnit1.setId(null);
        assertThat(courseUnit1).isNotEqualTo(courseUnit2);
    }
}
