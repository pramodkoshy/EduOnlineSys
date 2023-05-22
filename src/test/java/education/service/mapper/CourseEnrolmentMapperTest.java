package education.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseEnrolmentMapperTest {

    private CourseEnrolmentMapper courseEnrolmentMapper;

    @BeforeEach
    public void setUp() {
        courseEnrolmentMapper = new CourseEnrolmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(courseEnrolmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(courseEnrolmentMapper.fromId(null)).isNull();
    }
}
