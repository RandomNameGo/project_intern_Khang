package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllTaskInCourseResponse;

import java.util.List;

public interface TaskService {
    Task createTask(CreateTaskRequest createTaskRequest, int courseId);

    GetAllTaskInCourseResponse getTasks(int courseId, int pageSize, int pageNo);
}
