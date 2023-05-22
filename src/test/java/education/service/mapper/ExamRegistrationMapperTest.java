package education.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExamRegistrationMapperTest {

    private ExamRegistrationMapper examRegistrationMapper;

    @BeforeEach
    public void setUp() {
        examRegistrationMapper = new ExamRegistrationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(examRegistrationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(examRegistrationMapper.fromId(null)).isNull();
    }
}
