package swp.internmanagement.internmanagement.entity;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    @JsonBackReference
    private Field field;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "job_name", nullable = false, length = 50)
    private String jobName;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "job_description", nullable = false)
    private String jobDescription;
}