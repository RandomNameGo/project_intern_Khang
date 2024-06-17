package swp.internmanagement.internmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.CourseInternId;

public interface CourseInternRepository extends JpaRepository<CourseIntern, CourseInternId> {

    List<CourseIntern> findByInternId(int internId);

    List<CourseIntern> findByCourseId(int courseId);
}