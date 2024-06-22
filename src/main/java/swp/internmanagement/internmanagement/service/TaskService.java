package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllTaskInCourseResponse;

import java.util.List;

public interface TaskService {
    String createTask(CreateTaskRequest createTaskRequest, int courseId) throws Exception;

    List<Task> getTasks(int courseId, int mentorId);

    boolean deleteTask(int taskId);

    String updateTask(CreateTaskRequest createTaskRequest, Integer id) throws Exception;
}
