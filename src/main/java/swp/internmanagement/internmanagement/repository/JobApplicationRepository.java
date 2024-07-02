package swp.internmanagement.internmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import swp.internmanagement.internmanagement.entity.JobApplication;

@Repository
public interface JobApplicationRepository extends JpaRepository <JobApplication,Integer>{
    @Query("select ja from JobApplication ja where ja.status = 1")
    Page<JobApplication> findAcceptedJobApplications(Pageable pageable);

    @Query("SELECT ja FROM JobApplication ja JOIN FETCH ja.job j JOIN FETCH j.company c WHERE ja.status = :status")
    Page<JobApplication> findByStatus(Integer status, Pageable pageable);
    @Query("select ja from JobApplication ja " +
    "left join ja.job jb " +
    "left join ja.schedules s " +
    "where s.id is null and jb.company.id = :companyId and ja.status = :status")
    Page<JobApplication> findByJob(Integer companyId, Integer status, Pageable pageable);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.job.company.id = :companyId AND (ja.status IS NULL OR ja.status = :status OR ja.status=2 OR ja.status=3 OR ja.status=4) ORDER BY ja.id DESC")
    Page<JobApplication> findAllJob(Integer companyId, Integer status, Pageable pageable);
    // @Query("select ja from JobApplication ja where ja.status = 1 AND ja.companyId=:companyId")
    // Page<JobApplication> findByJob(Integer companyId, Integer status, Pageable pageable);
    @Query("SELECT ja FROM JobApplication ja WHERE ja.job.company.id = :companyId AND (ja.status IS NULL OR ja.status = :status OR ja.status=3)  AND ja.schedules IS NULL")
    Page<JobApplication> findByJobCompanyIdAndStatusAndSchedulesIsNull(Integer companyId, Integer status, Pageable pageable);

}
