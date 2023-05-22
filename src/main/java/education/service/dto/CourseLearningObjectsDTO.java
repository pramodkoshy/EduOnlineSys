package education.service.dto;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link education.domain.CourseLearningObjects} entity.
 */
@Introspected
public class CourseLearningObjectsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Size(min = 1, max = 50)
    private String type;

    
    @Lob
    private String text;

    @NotNull
    @Size(min = 5, max = 1000)
    private String url;


    private Long courseUnitId;

    private String courseUnitName;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCourseUnitId() {
        return courseUnitId;
    }

    public void setCourseUnitId(Long courseUnitId) {
        this.courseUnitId = courseUnitId;
    }

    public String getCourseUnitName() {
        return courseUnitName;
    }

    public void setCourseUnitName(String courseUnitName) {
        this.courseUnitName = courseUnitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseLearningObjectsDTO courseLearningObjectsDTO = (CourseLearningObjectsDTO) o;
        if (courseLearningObjectsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseLearningObjectsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseLearningObjectsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", text='" + getText() + "'" +
            ", url='" + getUrl() + "'" +
            ", courseUnitId=" + getCourseUnitId() +
            ", courseUnitName='" + getCourseUnitName() + "'" +
            "}";
    }
}
