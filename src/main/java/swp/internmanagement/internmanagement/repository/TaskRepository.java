package swp.internmanagement.internmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("select t from Task t where t.course.id = :courseId")
    Page<Task> findAllInCourse(int courseId ,Pageable pageable);
}