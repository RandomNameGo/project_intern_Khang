package swp.internmanagement.internmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "job_application")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_application_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    @JsonBackReference
    private Job job; 

    @Column(name = "email")
    private String email;

    @Column(name = "full_Name")
    private String fullName;
    
    @Lob
    @Column(name = "CV")
    private byte[] CV;
    
    @Column(name = "status")
    private int status;

    public JobApplication(@NotNull Job job, String email, String fullName, byte[] cV, int status) {
        this.job = job;
        this.email = email;
        this.fullName = fullName;
        CV = cV;
        this.status = status;
    }
}
