package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("select t from Task t where t.course.id = :courseId")
    List<Task> findAllInCourse(int courseId);
}