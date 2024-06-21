package swp.internmanagement.internmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.CourseInternId;

public interface CourseInternRepository extends JpaRepository<CourseIntern, CourseInternId> {

    @Query("select c from CourseIntern c where c.course.status = 1 and c.intern.id = :internId")
    List<CourseIntern> findByInternId(int internId);

    List<CourseIntern> findByCourseId(int courseId);
}