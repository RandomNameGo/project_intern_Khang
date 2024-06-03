package swp.internmanagement.internmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import swp.internmanagement.internmanagement.models.UserAccount;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mentor_id", nullable = false)
    private UserAccount mentor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "course_description", nullable = false)
    private String courseDescription;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Size(max = 10)
    @Nationalized
    @Column(name = "status", length = 10)
    private String status;

}