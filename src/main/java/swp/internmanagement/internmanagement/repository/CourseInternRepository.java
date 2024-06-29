package swp.internmanagement.internmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.CourseInternId;

public interface CourseInternRepository extends JpaRepository<CourseIntern, CourseInternId> {

    @Query("select c from CourseIntern c where c.course.status = 1 and c.intern.id = :internId and c.intern.status is not null")
    List<CourseIntern> findByInternId(int internId);

    @Query("select c from CourseIntern c where c.course.status = 1 and c.course.id = :courseId and c.intern.status is not null")
    List<CourseIntern> findByCourseId(int courseId);

    @Query("select c from CourseIntern c where c.intern.id = :internId and c.course.id = :courseId and c.intern.status is not null")
    CourseIntern findByInternIdAndCourseId(int internId, int courseId);

    @Query("select c from CourseIntern c where c.course.status is null and c.course.id = :courseId and c.intern.status is not null")
    List<CourseIntern> findByEndCourseId(int courseId);
}