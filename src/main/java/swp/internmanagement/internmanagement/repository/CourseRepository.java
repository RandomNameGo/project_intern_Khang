package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}