package swp.internmanagement.internmanagement.payload.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobApplicationRequest {
    private int jobId;
    private String email;
    private String fullName;
    private MultipartFile CV; 

}
