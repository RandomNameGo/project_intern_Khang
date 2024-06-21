package swp.internmanagement.internmanagement.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.CourseInternId;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.repository.CourseInternRepository;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.List;

@Service
public class CourseInternServiceImpl implements CourseInternService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseInternRepository courseInternRepository;

    @Autowired
    private InternTaskService internTaskService;

    //This method is used by coordinator to add interns to course
    @Override
    public String addInternToCourse(AddInternToCourseRequest request, int courseId) {
        int[] internId = request.getInternId();
        CourseIntern courseIntern = new CourseIntern();
        if(!courseRepository.existsById(courseId)){
            return "Course not found";
        }
        Course course = courseRepository.findById(courseId).get();
        CourseInternId courseInternId = new CourseInternId();
        courseIntern.setCourse(course);
        courseInternId.setCourseId(courseId);
        for (int i : internId) {
            UserAccount userAccount = userRepository.findById(i).get();
            courseIntern.setIntern(userAccount);
            courseInternId.setInternId(i);
            courseIntern.setId(courseInternId);
            courseIntern.setResult(0.0);
            courseInternRepository.save(courseIntern);
        }
        return "Added successfully";
    }

    @Override
    public List<CourseIntern> geCoursesByInternId(int internId) {
        return courseInternRepository.findByInternId(internId);
    }

    @Override
    public void updateResult(int internId, int courseId) {
        CourseInternId courseInternId = new CourseInternId();
        courseInternId.setCourseId(courseId);
        courseInternId.setInternId(internId);
        if(!courseInternRepository.existsById(courseInternId)){
            return;
        }
        CourseIntern courseIntern = courseInternRepository.findById(courseInternId).get();
        double result = internTaskService.calculateTotalInternTaskResult(internId, courseId);
        courseIntern.setResult(result);
        courseInternRepository.save(courseIntern);
    }
}
