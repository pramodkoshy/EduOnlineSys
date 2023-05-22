package education.service.dto;

import io.micronaut.core.annotation.Introspected;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link education.domain.CourseEnrolment} entity.
 */
@Introspected
public class CourseEnrolmentDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate enrolmentDate;


    private Long studentId;

    private String studentName;

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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseEnrolmentDTO courseEnrolmentDTO = (CourseEnrolmentDTO) o;
        if (courseEnrolmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseEnrolmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseEnrolmentDTO{" +
            "id=" + getId() +
            ", enrolmentDate='" + getEnrolmentDate() + "'" +
            ", studentId=" + getStudentId() +
            ", studentName='" + getStudentName() + "'" +
            "}";
    }
}
