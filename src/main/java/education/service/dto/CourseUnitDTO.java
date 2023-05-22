package education.service.dto;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link education.domain.CourseUnit} entity.
 */
@Introspected
public class CourseUnitDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Size(min = 1, max = 500)
    private String description;


    private Long courseModuleId;

    private String courseModuleName;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCourseModuleId() {
        return courseModuleId;
    }

    public void setCourseModuleId(Long courseModuleId) {
        this.courseModuleId = courseModuleId;
    }

    public String getCourseModuleName() {
        return courseModuleName;
    }

    public void setCourseModuleName(String courseModuleName) {
        this.courseModuleName = courseModuleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseUnitDTO courseUnitDTO = (CourseUnitDTO) o;
        if (courseUnitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseUnitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseUnitDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", courseModuleId=" + getCourseModuleId() +
            ", courseModuleName='" + getCourseModuleName() + "'" +
            "}";
    }
}
