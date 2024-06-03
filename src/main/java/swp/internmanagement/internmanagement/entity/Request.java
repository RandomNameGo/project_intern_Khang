package swp.internmanagement.internmanagement.entity;

import org.hibernate.annotations.Nationalized;

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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.internmanagement.internmanagement.models.UserAccount;

@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Getter
@Setter
@Entity
@Table(name="request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserAccount user;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "request_type", nullable = false, length = 50)
    private String requestType;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "request_content", nullable = false)
    private String requestContent;

    @Column(name = "request_status", nullable = false)
    private Boolean requestStatus = false;
}