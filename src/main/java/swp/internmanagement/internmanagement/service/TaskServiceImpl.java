package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Task createTask(CreateTaskRequest createTaskRequest, int courseId) {
        Task task = new Task();
        if(!courseRepository.existsById(courseId)){
            return null;
        }
        Course course = courseRepository.findById(courseId).get();
        task.setCourse(course);
        task.setTaskContent(createTaskRequest.getTaskContent());
        task.setStartDate(createTaskRequest.getStartDate());
        task.setEndDate(createTaskRequest.getEndDate());
        taskRepository.save(task);
        return task;
    }
}
