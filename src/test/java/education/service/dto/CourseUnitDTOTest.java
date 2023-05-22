package education.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class CourseUnitDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseUnitDTO.class);
        CourseUnitDTO courseUnitDTO1 = new CourseUnitDTO();
        courseUnitDTO1.setId(1L);
        CourseUnitDTO courseUnitDTO2 = new CourseUnitDTO();
        assertThat(courseUnitDTO1).isNotEqualTo(courseUnitDTO2);
        courseUnitDTO2.setId(courseUnitDTO1.getId());
        assertThat(courseUnitDTO1).isEqualTo(courseUnitDTO2);
        courseUnitDTO2.setId(2L);
        assertThat(courseUnitDTO1).isNotEqualTo(courseUnitDTO2);
        courseUnitDTO1.setId(null);
        assertThat(courseUnitDTO1).isNotEqualTo(courseUnitDTO2);
    }
}
