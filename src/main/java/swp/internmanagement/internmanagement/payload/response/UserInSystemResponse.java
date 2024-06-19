package swp.internmanagement.internmanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInSystemResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String role;
    private String companyName;
    private Integer company_id;
}
