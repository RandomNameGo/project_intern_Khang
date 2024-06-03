package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.payload.response.GetAllJobsResponse;
import swp.internmanagement.internmanagement.payload.response.SearchJobsResponse;
import swp.internmanagement.internmanagement.repository.JobRepository;

import java.util.List;

@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobRepository jobRepository;

    @Override
    public GetAllJobsResponse getAllJobs(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Job> jobs = jobRepository.findAll(pageable);
        List<Job> listOfJobs = jobs.getContent();

        GetAllJobsResponse getAllJobsResponse = new GetAllJobsResponse();
        getAllJobsResponse.setJobs(listOfJobs);
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

}
