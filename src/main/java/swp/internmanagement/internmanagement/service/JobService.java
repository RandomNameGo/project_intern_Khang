package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.payload.response.GetAllJobsResponse;
import swp.internmanagement.internmanagement.payload.response.SearchJobsResponse;

public interface JobService {

    GetAllJobsResponse getAllJobs(int pageNo, int pageSize);

    SearchJobsResponse getJobs(String jobName, int pageNo, int pageSize);

    Job getJobId(Integer id);
}
