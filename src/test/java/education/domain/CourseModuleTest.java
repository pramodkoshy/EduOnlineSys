package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseModuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseModule.class);
        CourseModule courseModule1 = new CourseModule();
        courseModule1.setId(1L);
        CourseModule courseModule2 = new CourseModule();
        courseModule2.setId(courseModule1.getId());
        assertThat(courseModule1).isEqualTo(courseModule2);
        courseModule2.setId(2L);
        assertThat(courseModule1).isNotEqualTo(courseModule2);
        courseModule1.setId(null);
        assertThat(courseModule1).isNotEqualTo(courseModule2);
    }
}
