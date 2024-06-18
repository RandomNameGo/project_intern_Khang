package swp.internmanagement.internmanagement.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.models.JobApplicationDTO;
@Getter
@Setter
public class AcceptedJobApplicationResponse {
    private List<JobApplicationDTO> jobApplications;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
