package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.entity.InternTaskId;
import swp.internmanagement.internmanagement.entity.Task;
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
        List<InternTask> internTasks = internTaskRepository.findByCourseId(courseId, internId);
        List<InternTaskResponse> responses = new ArrayList<>();
        for (InternTask internTask : internTasks) {
            InternTaskResponse internTaskResponse = new InternTaskResponse();
            internTaskResponse.setTaskId(internTask.getTask().getId());
            internTaskResponse.setTaskId(internTask.getTask().getCourse().getId());
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
}
