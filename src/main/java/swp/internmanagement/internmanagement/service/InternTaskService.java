package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.response.InternTaskResponse;
import swp.internmanagement.internmanagement.payload.response.ShowInternTaskResponse;

import java.util.List;

public interface InternTaskService {

    ShowInternTaskResponse getInternTaskByCourseId(int courseId, int internId);

    void addInternToTask(Task task);

    String updateInternTask(int taskId, int internId);

    double calculateTotalInternTaskResult(int internId, int courseId);

    InternTask getInternTaskByInternId(int internId, int internTaskId);

    List<InternTask> getInternTaskByCourseId(int courseId);

    double getTotalInternTaskResult(int internId);
}
