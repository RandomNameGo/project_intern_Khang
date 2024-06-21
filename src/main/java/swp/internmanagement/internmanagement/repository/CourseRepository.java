package swp.internmanagement.internmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("select c from Course c where c.mentor.id = :mentorId")
    List<Course> findByMentor(int mentorId);

    @Query("select c from Course c where c.company.id = :companyId")
    Page<Course> findByCompany(int companyId, Pageable pageable);
    Page<Course> findByMentorId(Integer mentorId, Pageable pageable);

}