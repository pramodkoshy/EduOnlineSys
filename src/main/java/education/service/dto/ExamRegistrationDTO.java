package education.service.dto;

import io.micronaut.core.annotation.Introspected;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link education.domain.ExamRegistration} entity.
 */
@Introspected
public class ExamRegistrationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate enrolmentDate;


    private Long teacherId;

    private String teacherName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEnrolmentDate() {
        return enrolmentDate;
    }

    public void setEnrolmentDate(LocalDate enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExamRegistrationDTO examRegistrationDTO = (ExamRegistrationDTO) o;
        if (examRegistrationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examRegistrationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamRegistrationDTO{" +
            "id=" + getId() +
            ", enrolmentDate='" + getEnrolmentDate() + "'" +
            ", teacherId=" + getTeacherId() +
            ", teacherName='" + getTeacherName() + "'" +
            "}";
    }
}
