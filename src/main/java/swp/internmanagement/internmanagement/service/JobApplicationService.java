package swp.internmanagement.internmanagement.service;

import java.util.Optional;

import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.payload.response.AcceptedJobApplicationResponse;
import swp.internmanagement.internmanagement.payload.response.JobApplicationResponse;

public interface JobApplicationService {
    boolean addJobApplication(JobApplicationRequest jobApplicationRequest);

    Optional<JobApplication> getJobApplicationById(Integer id);

    JobApplicationResponse getAllJobApplication(int pageNo, int pageSize, int id);

    String updateJobApplication(Integer id, Integer status);

    AcceptedJobApplicationResponse getAcceptedJobApplication(int pageNo, int pageSize);
}
