package swp.internmanagement.internmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostJobApplicationRequest {
    private int field_id;
    private int company_id;
    private String job_name;
    private String job_description;
}
