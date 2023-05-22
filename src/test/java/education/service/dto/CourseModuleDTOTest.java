package education.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseModuleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseModuleDTO.class);
        CourseModuleDTO courseModuleDTO1 = new CourseModuleDTO();
        courseModuleDTO1.setId(1L);
        CourseModuleDTO courseModuleDTO2 = new CourseModuleDTO();
        assertThat(courseModuleDTO1).isNotEqualTo(courseModuleDTO2);
        courseModuleDTO2.setId(courseModuleDTO1.getId());
        assertThat(courseModuleDTO1).isEqualTo(courseModuleDTO2);
        courseModuleDTO2.setId(2L);
        assertThat(courseModuleDTO1).isNotEqualTo(courseModuleDTO2);
        courseModuleDTO1.setId(null);
        assertThat(courseModuleDTO1).isNotEqualTo(courseModuleDTO2);
    }
}
