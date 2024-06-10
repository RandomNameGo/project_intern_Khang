package swp.internmanagement.internmanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Integer user_id;
    private String username;
    private String email;
    private String role;
    private int company_id;
    private String fullName;
}
