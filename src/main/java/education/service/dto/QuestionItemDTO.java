package education.service.dto;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link education.domain.QuestionItem} entity.
 */
@Introspected
public class QuestionItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    private Integer number;


    private Long answerItemId;

    private String answerItemNumber;

    private Long questionId;

    private String questionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getAnswerItemId() {
        return answerItemId;
    }

    public void setAnswerItemId(Long answerItemId) {
        this.answerItemId = answerItemId;
    }

    public String getAnswerItemNumber() {
        return answerItemNumber;
    }

    public void setAnswerItemNumber(String answerItemNumber) {
        this.answerItemNumber = answerItemNumber;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionItemDTO questionItemDTO = (QuestionItemDTO) o;
        if (questionItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questionItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuestionItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", number=" + getNumber() +
            ", answerItemId=" + getAnswerItemId() +
            ", answerItemNumber='" + getAnswerItemNumber() + "'" +
            ", questionId=" + getQuestionId() +
            ", questionName='" + getQuestionName() + "'" +
            "}";
    }
}
