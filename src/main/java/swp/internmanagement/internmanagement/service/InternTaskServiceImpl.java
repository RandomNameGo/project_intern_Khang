package swp.internmanagement.internmanagement.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.*;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.response.InternTaskResponse;
import swp.internmanagement.internmanagement.payload.response.ShowInternTaskResponse;
import swp.internmanagement.internmanagement.repository.CourseInternRepository;
import swp.internmanagement.internmanagement.repository.InternTaskRepository;
import swp.internmanagement.internmanagement.repository.TaskRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternTaskServiceImpl implements InternTaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    CourseInternRepository courseInternRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InternTaskRepository internTaskRepository;

    @Override
    public ShowInternTaskResponse getInternTaskByCourseId(int courseId, int internId) {
        CourseInternId courseInternId = new CourseInternId();
        courseInternId.setCourseId(courseId);
        courseInternId.setInternId(internId);
        if(!courseInternRepository.existsById(courseInternId)) {
            return null;
        }
        List<InternTask> internTasks = internTaskRepository.findByCourseId(courseId, internId);
        List<InternTaskResponse> responses = new ArrayList<>();
        for (InternTask internTask : internTasks) {
            InternTaskResponse internTaskResponse = new InternTaskResponse();
            internTaskResponse.setTaskId(internTask.getTask().getId());
            internTaskResponse.setCourseId(internTask.getTask().getCourse().getId());
            internTaskResponse.setTaskContent(internTask.getTask().getTaskContent());
            internTaskResponse.setStartDate(internTask.getTask().getStartDate());
            internTaskResponse.setEndDate(internTask.getTask().getEndDate());
            internTaskResponse.setStatus(internTask.getTaskStatus());
            responses.add(internTaskResponse);
        }
        ShowInternTaskResponse showInternTaskResponse = new ShowInternTaskResponse();
        showInternTaskResponse.setInternTasks(responses);
        return showInternTaskResponse;
    }

    @Override
    public void addInternToTask(Task task) {
        int courseId = task.getCourse().getId();
        List<CourseIntern> courseInterns = courseInternRepository.findByCourseId(courseId);
        InternTask internTask = new InternTask();
        internTask.setTask(task);
        InternTaskId internTaskId = new InternTaskId();
        internTaskId.setTaskId(task.getId());
        for (CourseIntern courseIntern : courseInterns) {
            UserAccount intern = userRepository.findById(courseIntern.getIntern().getId()).get();
            internTask.setIntern(intern);
            internTaskId.setInternId(courseIntern.getIntern().getId());
            internTask.setId(internTaskId);
            internTaskRepository.save(internTask);
        }
    }

    @Override
    public String updateInternTask(int taskId, int internId) {
        InternTask internTask = internTaskRepository.findByTaskIdAndInternId(taskId, internId);
        internTask.setTaskStatus(Boolean.TRUE);
        internTaskRepository.save(internTask);
        return "You have completed the task";
    }

    @Override
    public double calculateTotalInternTaskResult(int internId, int courseId) {
        long totalTask = internTaskRepository.countInternTasksByInternId(internId, courseId);
        long totalCompletedTask = internTaskRepository.countInternTasksCompletedByInternId(internId, courseId);
        double totalTaskDouble = (double) totalTask;
        double totalCompletedTaskDouble = (double) totalCompletedTask;
        return (totalCompletedTaskDouble/totalTaskDouble)*100;
    }

    @Override
    public InternTask getInternTaskByInternId(int internId, int taskId) {
        InternTask internTask = internTaskRepository.findByTaskIdAndInternId(taskId, internId);
        return internTask;
    }

    @Override
    public List<InternTask> getInternTaskByCourseId(int courseId) {
        return internTaskRepository.findInternTasksByCourseId(courseId);
    }

    @Override
    public double getTotalInternTaskResult(int internId) {
        long totalTask = internTaskRepository.countAllInternTasks(internId);
        long totalCompletedTask = internTaskRepository.countInternTasksCompletedByInternId(internId, 0);
        double totalTaskDouble = (double) totalTask;
        double totalCompletedTaskDouble = (double) totalCompletedTask;
        return (totalCompletedTaskDouble/totalTaskDouble)*100;
    }


}
