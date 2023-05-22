package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseLearningObjectsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseLearningObjects.class);
        CourseLearningObjects courseLearningObjects1 = new CourseLearningObjects();
        courseLearningObjects1.setId(1L);
        CourseLearningObjects courseLearningObjects2 = new CourseLearningObjects();
        courseLearningObjects2.setId(courseLearningObjects1.getId());
        assertThat(courseLearningObjects1).isEqualTo(courseLearningObjects2);
        courseLearningObjects2.setId(2L);
        assertThat(courseLearningObjects1).isNotEqualTo(courseLearningObjects2);
        courseLearningObjects1.setId(null);
        assertThat(courseLearningObjects1).isNotEqualTo(courseLearningObjects2);
    }
}
