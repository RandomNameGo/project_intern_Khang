package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllTaskInCourseResponse;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TaskRepository taskRepository;

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

    @Override
    public GetAllTaskInCourseResponse getTasks(int courseId, int pageNo, int pageSize) {
        if(!courseRepository.existsById(courseId)){
            return null;
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> tasks = taskRepository.findAllInCourse(courseId, pageable);
        List<Task> taskList = tasks.getContent();

        GetAllTaskInCourseResponse getAllTaskInCourseResponse = new GetAllTaskInCourseResponse();
        getAllTaskInCourseResponse.setTasks(taskList);
        getAllTaskInCourseResponse.setPageNo(tasks.getNumber());
        getAllTaskInCourseResponse.setPageSize(tasks.getSize());
        getAllTaskInCourseResponse.setTotalItems(tasks.getTotalElements());
        getAllTaskInCourseResponse.setTotalPages(tasks.getTotalPages());

        return getAllTaskInCourseResponse;
    }


}
