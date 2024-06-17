package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.response.GetCourseNameResponse;
import swp.internmanagement.internmanagement.repository.CompanyRepository;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.time.LocalDate;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Course addCourse(CreateCourseRequest createCourseRequest, int companyId) {
        int mentorId = createCourseRequest.getMentorId();
        UserAccount mentorAccount = userRepository.findById(mentorId).get();
        Company company = companyRepository.findById(companyId).get();
        Course course = new Course();
        course.setCompany(company);
        course.setMentor(mentorAccount);
        course.setCourseDescription(createCourseRequest.getCourseDescription());
        course.setStartDate(createCourseRequest.getStartDate());
        course.setEndDate(createCourseRequest.getEndDate());
        course.setStatus(0);
        courseRepository.save(course);
        return course;
    }

    @Override
    public Course getCourse(int courseId) {
        return courseRepository.findById(courseId).get();
    }

    @Override
    public GetCourseNameResponse getCourseName(int courseId) {
        if(!courseRepository.existsById(courseId)) {
            return null;
        }
        Course course = courseRepository.findById(courseId).get();
        GetCourseNameResponse getCourseNameResponse = new GetCourseNameResponse();
        getCourseNameResponse.setCourseName(course.getCourseDescription());
        return getCourseNameResponse;
    }
}
