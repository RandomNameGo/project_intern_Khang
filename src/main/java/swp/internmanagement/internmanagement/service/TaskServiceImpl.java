package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
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

    @Autowired
    private InternTaskService internTaskService;

    @Override
    public String createTask(CreateTaskRequest createTaskRequest, int courseId) {
        Task task = new Task();
        if(!courseRepository.existsById(courseId)){
            return null;
        }

        //convert string to LocalDate
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(createTaskRequest.getStartDate(), inputFormatter);
        LocalDate endDate = LocalDate.parse(createTaskRequest.getEndDate(), inputFormatter);
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        LocalDate startDateAfterConvert = LocalDate.parse(formattedStartDate, formatter);
        LocalDate endDateAfterConvert = LocalDate.parse(formattedEndDate, formatter);
        //validation
        if(startDateAfterConvert.isAfter(endDateAfterConvert)){
            return "Start date cant not be after end date";
        }
        if(startDateAfterConvert.isBefore(LocalDate.now())){
            return "Start date can not be before current date";
        }
        if(endDateAfterConvert.isBefore(LocalDate.now())){
            return "End date can not be before current date";
        }

        Course course = courseRepository.findById(courseId).get();

        //validate
        if(startDateAfterConvert.isBefore(course.getStartDate())){
            return "Start date can not be before course start date";
        }
        if(endDateAfterConvert.isAfter(course.getEndDate())){
            return "End date can not be after course end date";
        }

        task.setCourse(course);
        task.setTaskContent(createTaskRequest.getTaskContent());
        task.setStartDate(startDateAfterConvert);
        task.setEndDate(endDateAfterConvert);
        taskRepository.save(task);
        internTaskService.addInternToTask(task);
        return "Created task successfully";
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
