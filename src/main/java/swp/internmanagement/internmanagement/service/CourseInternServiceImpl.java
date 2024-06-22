package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.entity.CourseInternId;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.payload.response.GetInternResultFromCourseResponse;
import swp.internmanagement.internmanagement.payload.response.GetListInternResultFromCourseResponse;
import swp.internmanagement.internmanagement.repository.CourseInternRepository;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.InternTaskRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    private InternTaskRepository internTaskRepository;

    //This method is used by coordinator to add interns to course
    @Override
    public String addInternToCourse(List<AddInternToCourseRequest> request, int courseId) {
        CourseIntern courseIntern = new CourseIntern();
        if(!courseRepository.existsById(courseId)){
            return "Course not found";
        }
        Course course = courseRepository.findById(courseId).get();

        LocalDate localDate = LocalDate.now();
        if(course.getStartDate().isBefore(localDate)){
            return "Course already started";
        }

        CourseInternId courseInternId = new CourseInternId();
        courseIntern.setCourse(course);
        courseInternId.setCourseId(courseId);
        for (AddInternToCourseRequest a : request) {
            UserAccount userAccount = userRepository.findById(a.getInternId()).get();
            courseIntern.setIntern(userAccount);
            courseInternId.setInternId(a.getInternId());
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

    @Override
    public GetListInternResultFromCourseResponse getListInternResultFromCourse(int courseId, int mentorId) {

        Course course = courseRepository.findById(courseId).get();
        if(course.getMentor().getId() != mentorId){
            throw new RuntimeException("you are not allowed to access this course");
        }

        List<CourseIntern> courseInterns = courseInternRepository.findByCourseId(courseId);
        List<GetInternResultFromCourseResponse> result = new ArrayList<>();
        for (CourseIntern courseIntern : courseInterns) {
            GetInternResultFromCourseResponse getInternResultFromCourseResponse = new GetInternResultFromCourseResponse();
            getInternResultFromCourseResponse.setInternId(courseIntern.getIntern().getId());
            getInternResultFromCourseResponse.setInternName(courseIntern.getIntern().getFullName());
            getInternResultFromCourseResponse.setTotalTask(internTaskRepository.countInternTasksByInternId(courseIntern.getIntern().getId(), courseIntern.getCourse().getId()));
            getInternResultFromCourseResponse.setCompletedTasks(internTaskRepository.countInternTasksCompletedByInternId(courseIntern.getIntern().getId(), courseIntern.getCourse().getId()));
            getInternResultFromCourseResponse.setResult(courseIntern.getResult());
            result.add(getInternResultFromCourseResponse);
        }

        GetListInternResultFromCourseResponse getListInternResultFromCourseResponse = new GetListInternResultFromCourseResponse();
        getListInternResultFromCourseResponse.setGetListInternResultFromCourse(result);

        return getListInternResultFromCourseResponse;
    }
}
