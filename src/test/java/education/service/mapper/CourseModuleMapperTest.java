package education.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseModuleMapperTest {

    private CourseModuleMapper courseModuleMapper;

    @BeforeEach
    public void setUp() {
        courseModuleMapper = new CourseModuleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(courseModuleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(courseModuleMapper.fromId(null)).isNull();
    }
}
