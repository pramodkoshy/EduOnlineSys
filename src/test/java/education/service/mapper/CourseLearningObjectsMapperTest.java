package education.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseLearningObjectsMapperTest {

    private CourseLearningObjectsMapper courseLearningObjectsMapper;

    @BeforeEach
    public void setUp() {
        courseLearningObjectsMapper = new CourseLearningObjectsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(courseLearningObjectsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(courseLearningObjectsMapper.fromId(null)).isNull();
    }
}
