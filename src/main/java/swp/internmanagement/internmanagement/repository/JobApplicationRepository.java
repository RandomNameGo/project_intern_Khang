package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import swp.internmanagement.internmanagement.entity.JobApplication;

@Repository
public interface JobApplicationRepository extends JpaRepository <JobApplication,Integer>{
    
}
