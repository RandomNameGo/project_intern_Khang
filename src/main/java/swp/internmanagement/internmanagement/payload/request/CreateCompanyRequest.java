package swp.internmanagement.internmanagement.payload.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompanyRequest {
    private String companyName;
    private String companyDescription;
    private String location;
    private MultipartFile image;
}
