package swp.internmanagement.internmanagement.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;
import swp.internmanagement.internmanagement.entity.Request;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 50)
    @Nationalized
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Size(max = 50)
    @Nationalized
    @Column(name = "verification_code", length = 50)
    private String verificationCode;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "intern", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MentorFeedbackIntern> mentorFeedbackInterns = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private InternDetail internDetails;

    
}
