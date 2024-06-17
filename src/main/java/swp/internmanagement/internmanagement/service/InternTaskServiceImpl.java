package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.entity.InternTaskId;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.repository.*;

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
    public void getInternTaskByCourseId(int courseId) {

    }
}
