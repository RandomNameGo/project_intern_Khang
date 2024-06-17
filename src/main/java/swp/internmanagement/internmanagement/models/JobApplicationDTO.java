package swp.internmanagement.internmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationDTO {
    private Integer jobApplicationId;
    private String fullName;
    private String email;
    private Integer companyId;
    private String companyName;

}
