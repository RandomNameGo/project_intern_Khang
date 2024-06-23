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
import swp.internmanagement.internmanagement.entity.Field;
import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.models.JobApplicationDTO;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.payload.request.PostJobApplicationRequest;
import swp.internmanagement.internmanagement.payload.response.AcceptedJobApplicationResponse;
import swp.internmanagement.internmanagement.payload.response.JobApplicationResponse;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;
import swp.internmanagement.internmanagement.repository.JobRepository;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private JobRepository jobRepository;

    @Override
    public boolean addJobApplication(JobApplicationRequest jobApplicationRequest) {
        try {
            JobApplication jobApplication = new JobApplication();
            Job job = new Job();
            job.setId(jobApplicationRequest.getJobId());
            jobApplication.setJob(job);
            jobApplication.setEmail(jobApplicationRequest.getEmail());
            jobApplication.setFullName(jobApplicationRequest.getFullName());
            jobApplication.setCV(jobApplicationRequest.getCV().getBytes());
            jobApplication.setStatus(null);
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
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Company company = new Company();
        company.setId(id);
        Page<Job> jobResponse = jobRepository.findByCompanyId(company.getId(), pageable);
        List<Job> listJob = jobResponse.getContent();
        List<Job> jobsWithApplications = listJob.stream()
        .filter(job -> job.getJobApplications() != null && !job.getJobApplications().isEmpty())
        .map(job -> {
            job.setJobApplications(
                    job.getJobApplications().stream()
                            .filter(jobApplication -> jobApplication.getStatus() == null || jobApplication.getStatus() == 1)
                            .collect(Collectors.toList()));
            return job;
        })
        .filter(job -> !job.getJobApplications().isEmpty())
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
    public String updateJobApplication(Integer id, int status) {
        Optional<JobApplication> jobApplicationOptional = jobApplicationRepository.findById(id);
        String message = "";
        try {
            if (status == 1 || status == 0) {
                JobApplication jApplication = jobApplicationOptional.get();
                jApplication.setStatus(status);
                jobApplicationRepository.save(jApplication);
                message = "Sucess";
            } else {
                throw new Exception("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error";
        }
        return message;
    }

    @Override
    public AcceptedJobApplicationResponse getAcceptedJobApplication(int pageNo, int pageSize) {
        // Pageable pageable = PageRequest.of(pageNo, pageSize);
        // Page<JobApplication> jobApplications = jobApplicationRepository.findAcceptedJobApplications(pageable);
        // List<JobApplication> jobApplicationList = jobApplications.getContent();

        // AcceptedJobApplicationResponse response = new AcceptedJobApplicationResponse();
        // response.setJobApplications(jobApplicationList);
        // response.setPageSize(jobApplications.getSize());
        // response.setPageNo(jobApplications.getNumber());
        // response.setTotalItems(jobApplications.getTotalElements());
        // response.setTotalPages(jobApplications.getTotalPages());

        // return response;
        return null;
    }

    @Override
    public AcceptedJobApplicationResponse getAllAcceptedJobApplication(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByStatus(1, pageable);
        
        List<JobApplicationDTO> jobApplicationDTOs = jobApplicationPage.getContent().stream()
                .map(ja -> new JobApplicationDTO(
                        ja.getId(),
                        ja.getFullName(),
                        ja.getEmail(),
                        ja.getJob().getCompany().getId(),
                        ja.getJob().getCompany().getCompanyName()
                ))
                .collect(Collectors.toList());

        AcceptedJobApplicationResponse response = new AcceptedJobApplicationResponse();
        response.setJobApplications(jobApplicationDTOs);
        response.setPageSize(jobApplicationPage.getSize());
        response.setPageNo(jobApplicationPage.getNumber());
        response.setTotalItems(jobApplicationPage.getTotalElements());
        response.setTotalPages(jobApplicationPage.getTotalPages());
        
        return response;
    }
    @Override
    public boolean postJobApplication(PostJobApplicationRequest postJobApplicationRequest) {
        try {
            Job job = new Job();
            Company company =new Company();
            company.setId(postJobApplicationRequest.getCompany_id());
            Field field = new Field();
            field.setId(postJobApplicationRequest.getField_id());
            job.setCompany(company);
            job.setField(field);
            job.setJobDescription(postJobApplicationRequest.getJob_description());
            job.setJobName(postJobApplicationRequest.getJob_name());
            jobRepository.save(job);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public AcceptedJobApplicationResponse getAllAcceptedJobApplicationById(Integer companyId, int pageNo,
            int pageSize) {
                Pageable pageable = PageRequest.of(pageNo, pageSize);
                Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByJobCompanyIdAndStatusAndSchedulesIsNull(companyId, 1, pageable);
                
                List<JobApplicationDTO> jobApplicationDTOs = jobApplicationPage.getContent().stream()
                        .map(ja -> new JobApplicationDTO(
                                ja.getId(),
                                ja.getFullName(),
                                ja.getEmail(),
                                ja.getJob().getCompany().getId(),
                                ja.getJob().getCompany().getCompanyName()
                        ))
                        .collect(Collectors.toList());
        
                AcceptedJobApplicationResponse response = new AcceptedJobApplicationResponse();
                response.setJobApplications(jobApplicationDTOs);
                response.setPageSize(jobApplicationPage.getSize());
                response.setPageNo(jobApplicationPage.getNumber());
                response.setTotalItems(jobApplicationPage.getTotalElements());
                response.setTotalPages(jobApplicationPage.getTotalPages());
                
                return response;
    }
}
