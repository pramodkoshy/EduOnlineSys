package education.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CourseUnit.
 */
@Entity
@Table(name = "course_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @OneToMany(mappedBy = "courseUnit")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CourseLearningObjects> courseLearningObjects = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("courseUnits")
    private CourseModule courseModule;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CourseUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public CourseUnit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CourseLearningObjects> getCourseLearningObjects() {
        return courseLearningObjects;
    }

    public CourseUnit courseLearningObjects(Set<CourseLearningObjects> courseLearningObjects) {
        this.courseLearningObjects = courseLearningObjects;
        return this;
    }

    public CourseUnit addCourseLearningObjects(CourseLearningObjects courseLearningObjects) {
        this.courseLearningObjects.add(courseLearningObjects);
        courseLearningObjects.setCourseUnit(this);
        return this;
    }

    public CourseUnit removeCourseLearningObjects(CourseLearningObjects courseLearningObjects) {
        this.courseLearningObjects.remove(courseLearningObjects);
        courseLearningObjects.setCourseUnit(null);
        return this;
    }

    public void setCourseLearningObjects(Set<CourseLearningObjects> courseLearningObjects) {
        this.courseLearningObjects = courseLearningObjects;
    }

    public CourseModule getCourseModule() {
        return courseModule;
    }

    public CourseUnit courseModule(CourseModule courseModule) {
        this.courseModule = courseModule;
        return this;
    }

    public void setCourseModule(CourseModule courseModule) {
        this.courseModule = courseModule;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseUnit)) {
            return false;
        }
        return id != null && id.equals(((CourseUnit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CourseUnit{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
