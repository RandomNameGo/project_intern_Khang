package swp.internmanagement.internmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.Job;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    @Query("select j from Job j where j.jobName like  %?1%  or j.company.companyName like %?1% or j.field.fieldName like %?1%")
    Page<Job> findJobs(String name, Pageable pageable);

    @Query("select j from Job j where j.id = :id")
    Page<Job> findById(Integer id, Pageable pageable);
    Page<Job> findByCompanyId(Integer companyId, Pageable pageable);
    Page<Job> findByJobApplicationsIsNotNull(Pageable pageable);
}