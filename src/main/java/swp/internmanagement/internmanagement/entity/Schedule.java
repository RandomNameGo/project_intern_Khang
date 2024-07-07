package swp.internmanagement.internmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "application_id", nullable = false)
    @JsonBackReference
    private JobApplication application;

    @NotNull
    @Column(name = "schedule_time", nullable = false)
    private Instant scheduleTime;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "location", nullable = false, length = 50)
    private String location;

}