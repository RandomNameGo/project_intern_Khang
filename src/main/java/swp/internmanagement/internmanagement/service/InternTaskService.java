package swp.internmanagement.internmanagement.service;

import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.entity.Task;

public interface InternTaskService {
    void addInternToTask(Task task);

    @Query("select t from InternTask t where t.task.course.id = :courseId")
    void getInternTaskByCourseId(int courseId);
}
