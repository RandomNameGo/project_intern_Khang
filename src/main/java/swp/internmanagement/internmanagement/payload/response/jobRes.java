package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class jobRes {
    private int id;
    private String jobName;
    private String jobDescription;
    private CompanyRes company; 
}
