package swp.internmanagement.internmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import swp.internmanagement.internmanagement.models.UserAccount;

@Getter
@Setter
@Entity
@Table(name = "intern_detail")
public class InternDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intern_detail_id", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Nationalized
    @Lob
    @Column(name = "work_history")
    private String workHistory;

    @Nationalized
    @Lob
    @Column(name = "education_background")
    private String educationBackground;

}