package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;

public interface TaskService {
    Task createTask(CreateTaskRequest createTaskRequest, int courseId);
}
