package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Job;

import java.util.List;


@Getter
@Setter
public class SearchJobsResponse {
    private List<Job> jobs;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
