package swp.internmanagement.internmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import swp.internmanagement.internmanagement.entity.Job;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    @Query("select j from Job j where (j.jobName like %:name% or j.company.companyName like %:name%) and (:fieldId is null or j.field.id = :fieldId) ORDER BY j.id DESC")
    Page<Job> findJobs(String name,Integer fieldId, Pageable pageable);

    @Query("select j from Job j where j.id = :id")
    Page<Job> findById(Integer id, Pageable pageable);
    @Query("SELECT j from Job j where j.company.id =:companyId ORDER BY j.id DESC")
    Page<Job> findByCompanyId(Integer companyId, Pageable pageable);
    Page<Job> findByJobApplicationsIsNotNull(Pageable pageable);

    @Query("select j from Job j where j.company.id = :companyId")
    List<Job> findListByCompanyId(Integer companyId);
}