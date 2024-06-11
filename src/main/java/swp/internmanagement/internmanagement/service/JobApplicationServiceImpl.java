package swp.internmanagement.internmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;

@Service
public class JobApplicationServiceImpl implements JobApplicationService{
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Override
    public boolean addJobApplication(JobApplicationRequest jobApplicationRequest) {
        try {
            JobApplication jobApplication= new JobApplication();
            Job job =new Job();
            job.setId(jobApplicationRequest.getJobId());
            jobApplication.setJob(job);
            jobApplication.setEmail(jobApplicationRequest.getEmail());
            jobApplication.setFullName(jobApplicationRequest.getFullName());
            jobApplication.setCV(jobApplicationRequest.getCV().getBytes());
            jobApplication.setStatus(0); 
            jobApplicationRepository.save(jobApplication);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Optional<JobApplication> getJobApplicationById(Integer id) {
        return jobApplicationRepository.findById(id);
    }
}
