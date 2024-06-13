package swp.internmanagement.internmanagement.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Job;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponse {
    private List<Job> jobList;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
