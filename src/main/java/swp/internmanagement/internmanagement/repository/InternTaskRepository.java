package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.entity.InternTaskId;

import java.util.List;

public interface InternTaskRepository extends JpaRepository<InternTask, InternTaskId> {
     @Query("select t from InternTask t where t.task.course.id = :courseId and t.intern.id = :internId and t.intern.status is not null")
     List<InternTask> findByCourseId(int courseId, int internId);

     @Query("Select t from InternTask t where t.task.id = :taskId and t.intern.id = :internId and t.intern.status is not null")
     InternTask findByTaskIdAndInternId(int taskId, int internId);

     @Query("select count(t.task.id) from InternTask t where t.intern.id = :internId and t.task.course.id = :courseId and t.intern.status is not null")
     long countInternTasksByInternId(int internId, int courseId);

     @Query("select count(t.task.id) from InternTask t where t.intern.id = :internId and t.task.course.id = :courseId and t.taskStatus = true and t.intern.status is not null")
     long countInternTasksCompletedByInternId(int internId, int courseId);

     @Query("select t from InternTask t where t.task.course.id = :courseId and t.intern.status is not null")
     List<InternTask> findInternTasksByCourseId(int courseId);

     @Query("select count(t.task.id) from InternTask t where t.intern.id = :internId and t.intern.status is not null")
     long countAllInternTasks(int internId);

     @Query("select count(t.task.id) from InternTask t where t.intern.id = :internId and t.taskStatus = true and t.intern.status is not null")
     long countAllInternTasksCompletedByIntern(int internId);

     @Query("select i from InternTask i where i.intern.id = :internId and is.intern.status is not null")
     List<InternTask> findInternTasksByInternId(int internId);
}