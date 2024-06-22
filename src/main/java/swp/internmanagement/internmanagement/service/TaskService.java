package swp.internmanagement.internmanagement.service;

import java.util.List;

import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;

public interface TaskService {
    String createTask(CreateTaskRequest createTaskRequest, int courseId) throws Exception;

    List<Task> getTasks(int courseId, int mentorId);

    boolean deleteTask(int taskId);

    String updateTask(CreateTaskRequest createTaskRequest, Integer id) throws Exception;
}
