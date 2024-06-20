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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(createTaskRequest.getStartDate(), inputFormatter);
        LocalDate endDate = LocalDate.parse(createTaskRequest.getEndDate(), inputFormatter);

        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        LocalDate startDateAfter = LocalDate.parse(formattedStartDate, formatter);
        LocalDate endDateAfter = LocalDate.parse(formattedEndDate, formatter);

        Course course = courseRepository.findById(courseId).get();
        task.setCourse(course);
        task.setTaskContent(createTaskRequest.getTaskContent());
        task.setStartDate(startDateAfter);
        task.setEndDate(endDateAfter);
        taskRepository.save(task);
        return task;
    }

    @Override
    public List<Task> getTasks(int courseId, int mentorId) {
        if(!courseRepository.existsById(courseId)){
            return null;
        }

        Course course = courseRepository.findById(courseId).get();
        if(course.getMentor().getId() != mentorId){
            return null;
        }

        return taskRepository.findAllInCourse(courseId);
    }


}
