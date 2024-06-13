package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.JobApplication;

import java.util.List;
@Getter
@Setter
public class AcceptedJobApplicationResponse {
    private List<JobApplication> jobApplications;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
