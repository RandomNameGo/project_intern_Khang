package swp.internmanagement.internmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.models.UserAccount;

@Getter
@Setter
@Entity
@Table(name = "course_intern")
public class CourseIntern {
    @EmbeddedId
    private CourseInternId id;

    @MapsId("courseId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @MapsId("internId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "intern_id", referencedColumnName = "user_id", nullable = false)
    private UserAccount intern;

}