package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.payload.response.CompanyRes;
import swp.internmanagement.internmanagement.payload.response.GetAllJobRes;
import swp.internmanagement.internmanagement.payload.response.GetAllJobsResponse;
import swp.internmanagement.internmanagement.payload.response.SearchJobsResponse;
import swp.internmanagement.internmanagement.payload.response.jobRes;
import swp.internmanagement.internmanagement.repository.JobRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobRepository jobRepository;


    @Override
    public GetAllJobRes getAllJobs(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Job> jobs = jobRepository.findAll(pageable);
        List<Job> listOfJobs = jobs.getContent();
        List<jobRes> list = new ArrayList<>();
        for (Job job : listOfJobs) {
            jobRes jobRes = new jobRes();
            CompanyRes companyRes = new CompanyRes();
            companyRes.setCompanyDescription(job.getCompany().getCompanyDescription());
            companyRes.setCompanyName(job.getCompany().getCompanyName());
            companyRes.setId(job.getCompany().getId());
            companyRes.setLocation(job.getCompany().getLocation());
            jobRes.setCompany(companyRes);
            jobRes.setId(job.getId());
            jobRes.setJobDescription(job.getJobDescription());
            jobRes.setJobName(job.getJobName());
            list.add(jobRes);
        }
        GetAllJobRes getAllJobsResponse = new GetAllJobRes();
        getAllJobsResponse.setJobs(list);
        getAllJobsResponse.setPageNo(jobs.getNumber());
        getAllJobsResponse.setPageSize(jobs.getSize());
        getAllJobsResponse.setTotalItems(jobs.getTotalElements());
        getAllJobsResponse.setTotalPages(jobs.getTotalPages());

        return getAllJobsResponse;
    }

    @Override
    public SearchJobsResponse getJobs(String jobName, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Job> jobs = jobRepository.findJobs(jobName, pageable);
        List<Job> listOfJobs = jobs.getContent();

        SearchJobsResponse searchJobsResponse = new SearchJobsResponse();
        searchJobsResponse.setJobs(listOfJobs);
        searchJobsResponse.setPageNo(jobs.getNumber());
        searchJobsResponse.setPageSize(jobs.getSize());
        searchJobsResponse.setTotalItems(jobs.getTotalElements());
        searchJobsResponse.setTotalPages(jobs.getTotalPages());

        return searchJobsResponse;
    }

    @Override
    public Job getJobId(Integer id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.orElse(null);
    }

    @Override
    public Boolean deleteJob(Integer jobId) {
        if(!jobRepository.existsById(jobId)) {
            return false;
        }
        jobRepository.deleteById(jobId);
        return true;
    }

    @Override
    public GetAllJobsResponse getAllJobsByCompanyId(Integer companyId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Job> jobs = jobRepository.findByCompanyId(companyId, pageable);
        List<Job> listOfJobs = jobs.getContent();
        GetAllJobsResponse allJobsResponse= new GetAllJobsResponse();
        allJobsResponse.setJobs(listOfJobs);
        allJobsResponse.setPageNo(jobs.getNumber());
        allJobsResponse.setPageSize(jobs.getSize());
        allJobsResponse.setTotalItems(jobs.getTotalElements());
        allJobsResponse.setTotalPages(jobs.getTotalPages());
        return allJobsResponse;
    }
    @Override
    public boolean updateJob(Integer id, String discription) {
        try {
            Optional<Job> job=jobRepository.findById(id);
            Job jobData=job.get();
            jobData.setJobDescription(discription);
            if(job.isPresent()){
                jobRepository.save(jobData);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
}
