package education.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseUnitMapperTest {

    private CourseUnitMapper courseUnitMapper;

    @BeforeEach
    public void setUp() {
        courseUnitMapper = new CourseUnitMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(courseUnitMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(courseUnitMapper.fromId(null)).isNull();
    }
}
