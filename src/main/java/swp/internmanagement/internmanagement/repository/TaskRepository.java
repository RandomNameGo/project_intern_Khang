package swp.internmanagement.internmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import swp.internmanagement.internmanagement.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("select t from Task t where t.course.id = :courseId")
    List<Task> findAllInCourse(int courseId);

    @Query("select count(t.id) from Task t where t.course.id = :courseId")
    long countTaskBy(int  courseId);
}