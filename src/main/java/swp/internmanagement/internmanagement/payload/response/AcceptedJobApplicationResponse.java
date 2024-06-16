package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.models.JobApplicationDTO;

import java.util.List;
@Getter
@Setter
public class AcceptedJobApplicationResponse {
    private List<JobApplicationDTO> jobApplications;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
