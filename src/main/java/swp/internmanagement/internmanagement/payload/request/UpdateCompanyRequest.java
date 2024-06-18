package swp.internmanagement.internmanagement.payload.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCompanyRequest {

    private String companyName = "";

    private String description = "";

    private String location = "";
}
