package swp.internmanagement.internmanagement.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.CourseFeedback;
import swp.internmanagement.internmanagement.entity.CourseFeedbackId;
import swp.internmanagement.internmanagement.entity.CourseInternId;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.SendCourseFeedbackRequest;
import swp.internmanagement.internmanagement.repository.CourseFeedbackRepository;
import swp.internmanagement.internmanagement.repository.CourseInternRepository;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

@Service
public class CourseFeedbackServiceImpl implements CourseFeedbackService {

    @Autowired
    private CourseFeedbackRepository courseFeedbackRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseInternRepository courseInternRepository;

    @Override
    public String sendCourseFeedback(SendCourseFeedbackRequest sendCourseFeedbackRequest, int internId, int courseId) {
        if(!courseRepository.existsById(courseId)) {
            throw new RuntimeException( "Course not found");
        }
        CourseFeedback courseFeedback = new CourseFeedback();
        CourseFeedbackId courseFeedbackId = new CourseFeedbackId();
        courseFeedbackId.setCourseId(courseId);
        courseFeedbackId.setInternId(internId);
        CourseInternId courseInternId = new CourseInternId();
        courseInternId.setCourseId(courseId);
        courseInternId.setInternId(internId);
        Course course = courseRepository.findById(courseId).get();
        UserAccount userAccount = userRepository.findById(internId).get();

        if(!courseInternRepository.existsById(courseInternId)) {
            throw new RuntimeException( "You are not in this course");
        }

        if (courseFeedbackRepository.findById(courseFeedbackId).isPresent()) {
            throw new RuntimeException("You have already sent feedback about this course");
        }

        courseFeedback.setId(courseFeedbackId);
        courseFeedback.setCourse(course);
        courseFeedback.setIntern(userAccount);
        courseFeedback.setFeedbackContent(sendCourseFeedbackRequest.getContent());
        courseFeedbackRepository.save(courseFeedback);
        return "Send Feedback Successfully";
    }
}
