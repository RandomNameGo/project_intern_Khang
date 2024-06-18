package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.entity.InternTaskId;

import java.util.List;

public interface InternTaskRepository extends JpaRepository<InternTask, InternTaskId> {
     @Query("select t from InternTask t where t.task.course.id = :courseId")
     List<InternTask> findByCourseId(int courseId);

     @Query("Select t from InternTask t where t.task.id = :taskId and t.intern.id = :internId")
     InternTask findByTaskIdAndInternId(int taskId, int internId);
}