package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.payload.response.GetAllJobsResponse;

import java.util.List;

public interface JobService {

    GetAllJobsResponse getAllJobs(int pageNo, int pageSize);

    List<Job> getJobs(String jobName);
}
