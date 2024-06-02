package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.Job;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    @Query("select j from Job j where j.jobName like  %?1%  or j.company.companyName like %?1% or j.field.fieldName like %?1%")
    List<Job> findJobs(String name);
}