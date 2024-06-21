package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllTaskInCourseResponse;

import java.util.List;

public interface TaskService {
    String createTask(CreateTaskRequest createTaskRequest, int courseId);

    List<Task> getTasks(int courseId, int mentorId);
}
