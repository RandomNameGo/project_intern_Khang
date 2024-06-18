package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.response.ShowInternTaskResponse;

import java.util.List;

public interface InternTaskService {

    ShowInternTaskResponse getInternTaskByCourseId(int courseId);

    void addInternToTask(Task task);

    String updateInternTask(int taskId, int internId);
}
