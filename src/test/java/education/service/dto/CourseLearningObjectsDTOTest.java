package education.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseLearningObjectsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseLearningObjectsDTO.class);
        CourseLearningObjectsDTO courseLearningObjectsDTO1 = new CourseLearningObjectsDTO();
        courseLearningObjectsDTO1.setId(1L);
        CourseLearningObjectsDTO courseLearningObjectsDTO2 = new CourseLearningObjectsDTO();
        assertThat(courseLearningObjectsDTO1).isNotEqualTo(courseLearningObjectsDTO2);
        courseLearningObjectsDTO2.setId(courseLearningObjectsDTO1.getId());
        assertThat(courseLearningObjectsDTO1).isEqualTo(courseLearningObjectsDTO2);
        courseLearningObjectsDTO2.setId(2L);
        assertThat(courseLearningObjectsDTO1).isNotEqualTo(courseLearningObjectsDTO2);
        courseLearningObjectsDTO1.setId(null);
        assertThat(courseLearningObjectsDTO1).isNotEqualTo(courseLearningObjectsDTO2);
    }
}
