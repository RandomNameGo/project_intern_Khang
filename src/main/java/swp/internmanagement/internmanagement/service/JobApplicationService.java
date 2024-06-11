package swp.internmanagement.internmanagement.service;

import java.util.Optional;

import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;

public interface JobApplicationService {
    boolean addJobApplication(JobApplicationRequest jobApplicationRequest);

    Optional<JobApplication> getJobApplicationById(Integer id);
}
