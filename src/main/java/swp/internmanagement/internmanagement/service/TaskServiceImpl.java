package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.InternTask;
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

    @Autowired
    private CourseInternService courseInternService;

    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");


    @Override
    public String createTask(CreateTaskRequest createTaskRequest, int courseId) throws Exception {
    Task task = new Task();
    if (!courseRepository.existsById(courseId)) {
        return null;
    }

    Course course = courseRepository.findById(courseId).get();
    if(course.getStatus() != 1) {
        throw new RuntimeException("Course has not started yet");
    }

    String courseStartDate = course.getStartDate().format(inputFormatter);
    String courseEndDate = course.getEndDate().format(inputFormatter);


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate startDate = LocalDate.parse(createTaskRequest.getStartDate(), inputFormatter);
    LocalDate endDate = LocalDate.parse(createTaskRequest.getEndDate(), inputFormatter);
    String formattedStartDate = startDate.format(formatter);
    String formattedEndDate = endDate.format(formatter);
    LocalDate startDateAfterConvert = LocalDate.parse(formattedStartDate, formatter);
    LocalDate endDateAfterConvert = LocalDate.parse(formattedEndDate, formatter);
    if (startDateAfterConvert.isAfter(endDateAfterConvert)) {
        throw new Exception("Start date can't be after end date ");
    }
    if (startDateAfterConvert.isBefore(LocalDate.now())) {
        throw new Exception("Start date can't be before current date");
    }
    if (endDateAfterConvert.isBefore(LocalDate.now())) {
        throw new Exception("End date can't be before current date");
    }

    course = courseRepository.findById(courseId).orElseThrow(() -> new Exception("Course not found"));

    if (startDateAfterConvert.isBefore(course.getStartDate())) {
        throw new Exception("Start date can't be before course start date " + courseStartDate);
    }
    if (endDateAfterConvert.isAfter(course.getEndDate())) {
        throw new Exception("End date can't be after course end date " + courseEndDate);
    }
    if (startDateAfterConvert.isEqual(endDateAfterConvert)) {
        throw new Exception("Task must be at least one day");
    }
    task.setCourse(course);
    task.setTaskContent(createTaskRequest.getTaskContent());
    task.setStartDate(startDateAfterConvert);
    task.setEndDate(endDateAfterConvert);
    taskRepository.save(task);
    internTaskService.addInternToTask(task);
    List<InternTask> internTasks = internTaskService.getInternTaskByCourseId(task.getCourse().getId());
    for (InternTask internTask : internTasks) {
        courseInternService.updateResult(internTask.getIntern().getId(), internTask.getTask().getCourse().getId());
    }
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

    @Override
    public boolean deleteTask(int taskId) {
        if(!taskRepository.existsById(taskId)) {
            return false;
        }
        taskRepository.deleteById(taskId);
        return true;
    }
    @Override
    public String updateTask(CreateTaskRequest createTaskRequest, Integer id) throws Exception {
        Task task = taskRepository.findById(id).orElseThrow(() -> new Exception("Task not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(createTaskRequest.getStartDate(), inputFormatter);
        LocalDate endDate = LocalDate.parse(createTaskRequest.getEndDate(), inputFormatter);
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        LocalDate startDateAfterConvert = LocalDate.parse(formattedStartDate, formatter);
        LocalDate endDateAfterConvert = LocalDate.parse(formattedEndDate, formatter);
        if (startDateAfterConvert.isAfter(endDateAfterConvert)) {
            throw new Exception("Start date can't be after end date");
        }
        if (startDateAfterConvert.isBefore(LocalDate.now())) {
            throw new Exception("Start date can't be before current date");
        }
        if (endDateAfterConvert.isBefore(LocalDate.now())) {
            throw new Exception("End date can't be before current date");
        }
        Course course = courseRepository.findById(task.getCourse().getId()).orElseThrow(() -> new Exception("Course not found"));
        if (startDateAfterConvert.isBefore(course.getStartDate())) {
            throw new Exception("Start date can't be before course start date");
        }
        if (endDateAfterConvert.isAfter(course.getEndDate())) {
            throw new Exception("End date can't be after course end date");
        }
        task.setTaskContent(createTaskRequest.getTaskContent());
        task.setStartDate(startDateAfterConvert);
        task.setEndDate(endDateAfterConvert);
        taskRepository.save(task);
        return "Update task successfully";
    }
}
