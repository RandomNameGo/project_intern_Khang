package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRes {
    private int id;
    private String companyName;
    private String companyDescription;
    private String location;
}