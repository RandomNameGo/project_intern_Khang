package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.CourseInternId;

import java.util.List;

public interface CourseInternRepository extends JpaRepository<CourseIntern, CourseInternId> {

    List<CourseIntern> findByInternId(int internId);
}