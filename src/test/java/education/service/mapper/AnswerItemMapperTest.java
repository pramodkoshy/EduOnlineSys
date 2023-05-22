package education.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AnswerItemMapperTest {

    private AnswerItemMapper answerItemMapper;

    @BeforeEach
    public void setUp() {
        answerItemMapper = new AnswerItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(answerItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(answerItemMapper.fromId(null)).isNull();
    }
}
