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
}
