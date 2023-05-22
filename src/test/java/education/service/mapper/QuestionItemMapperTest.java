package education.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionItemMapperTest {

    private QuestionItemMapper questionItemMapper;

    @BeforeEach
    public void setUp() {
        questionItemMapper = new QuestionItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(questionItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(questionItemMapper.fromId(null)).isNull();
    }
}
