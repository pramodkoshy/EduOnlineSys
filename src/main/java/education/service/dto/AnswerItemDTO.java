package education.service.dto;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link education.domain.AnswerItem} entity.
 */
@Introspected
public class AnswerItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer number;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnswerItemDTO answerItemDTO = (AnswerItemDTO) o;
        if (answerItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), answerItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnswerItemDTO{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            "}";
    }
}
