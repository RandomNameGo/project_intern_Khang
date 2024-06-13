package swp.internmanagement.internmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.payload.response.JobApplicationResponse;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;
import swp.internmanagement.internmanagement.repository.JobRepository;

@Service
public class JobApplicationServiceImpl implements JobApplicationService{
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private JobRepository jobRepository;

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
    @Override
    public JobApplicationResponse getAllJobApplication(int pageNo, int pageSize, int id) {

        // Pageable pageable = PageRequest.of(pageNo, pageSize);
        // Company company = new Company();
        // company.setId(id);
        // Page<Job> jobResponse = jobRepository.findByCompanyId(company.getId(), pageable);
    
        // List<Job> listJob = jobResponse.getContent();
    
        // JobApplicationResponse response = new JobApplicationResponse();
        // response.setJobList(listJob);
        // response.setPageSize(jobResponse.getSize());
        // response.setPageNo(jobResponse.getNumber());
        // response.setTotalItems(jobResponse.getTotalElements());
        // response.setTotalPages(jobResponse.getTotalPages());
    
        // return response;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Company company = new Company();
        company.setId(id);
        Page<Job> jobResponse = jobRepository.findByCompanyId(company.getId(), pageable);
        List<Job> listJob = jobResponse.getContent();
        List<Job> jobsWithApplications = listJob.stream()
                .filter(job -> job.getJobApplications() != null && !job.getJobApplications().isEmpty())
                .collect(Collectors.toList());
        long totalJobApplications = jobsWithApplications.stream()
                .mapToLong(job -> job.getJobApplications().size())
                .sum();
        int totalPages = (int) Math.ceil((double) totalJobApplications / pageSize);
        JobApplicationResponse response = new JobApplicationResponse();
        response.setJobList(jobsWithApplications);
        response.setPageSize(jobResponse.getSize());
        response.setPageNo(jobResponse.getNumber());
        response.setTotalItems(totalJobApplications);
        response.setTotalPages(totalPages);
        return response;
    }
    @Override
    public String updateJobApplication(Integer id, Integer status) {
        Optional<JobApplication> jobApplicationOptional=jobApplicationRepository.findById(id);
        String message="";
        try {
            JobApplication jApplication=jobApplicationOptional.get();
            jApplication.setStatus(status);
            jobApplicationRepository.save(jApplication);
            message="Sucess";
        } catch (Exception e) {
            e.printStackTrace();
            message="Error";
        }
        return message;
    }
}
