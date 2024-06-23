package swp.internmanagement.internmanagement.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.CourseInternId;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.response.CourseResponse;
import swp.internmanagement.internmanagement.payload.response.CourseTaskResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllActivitiesInAllCourseResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllCourseByMentorIdResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllCourseInCompanyResponse;
import swp.internmanagement.internmanagement.payload.response.GetCourseNameResponse;
import swp.internmanagement.internmanagement.repository.CompanyRepository;
import swp.internmanagement.internmanagement.repository.CourseInternRepository;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.TaskRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CourseInternRepository courseInternRepository;

    @Autowired 
    private TaskRepository taskRepository;

    @Override
    public Course addCourse(CreateCourseRequest createCourseRequest, int companyId) {
        int mentorId = createCourseRequest.getMentorId();

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(createCourseRequest.getStartDate(), inputFormatter);
        LocalDate endDate = LocalDate.parse(createCourseRequest.getEndDate(), inputFormatter);

        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        LocalDate startDateAfter = LocalDate.parse(formattedStartDate, formatter);
        LocalDate endDateAfter = LocalDate.parse(formattedEndDate, formatter);

        UserAccount mentorAccount = userRepository.findById(mentorId).get();
        Company company = companyRepository.findById(companyId).get();
        Course course = new Course();
        course.setCompany(company);
        course.setMentor(mentorAccount);
        course.setCourseDescription(createCourseRequest.getCourseDescription());
        course.setStartDate(startDateAfter);
        course.setEndDate(endDateAfter);
        if(startDateAfter.isEqual(LocalDate.now())) {
            course.setStatus(1);
        }else{
            course.setStatus(0);
        }
       
        courseRepository.save(course);
        return course;
    }

    @Override
    public Course getCourse(int courseId) {
        return courseRepository.findById(courseId).get();
    }

    @Override
    public GetCourseNameResponse getCourseName(int courseId, int internId) {
        if(!courseRepository.existsById(courseId)) {
            return null;
        }
        CourseInternId courseInternId = new CourseInternId();
        courseInternId.setInternId(internId);
        courseInternId.setCourseId(courseId);
        if(!courseInternRepository.existsById(courseInternId)) {
            return null;
        }
        Course course = courseRepository.findById(courseId).get();
        GetCourseNameResponse getCourseNameResponse = new GetCourseNameResponse();
        getCourseNameResponse.setCourseName(course.getCourseDescription());
        return getCourseNameResponse;
    }

    @Override
    public String deleteCourse(int courseId) {
        if(!courseRepository.existsById(courseId)) {
            return "Course not found";
        }
        courseRepository.deleteById(courseId);
        return "Deleted course";
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCourseStatus() {
        LocalDate today = LocalDate.now();
        List<Course> courses = courseRepository.findAll();
        for(Course course : courses) {
            if(course.getEndDate().isAfter(today)) {
                course.setStatus(null);
            }
            if(course.getStartDate().equals(today)) {
                course.setStatus(1);
            }
            courseRepository.save(course);
        }

    }

    @Override
    public GetAllCourseByMentorIdResponse getCourseByMentor(int mentorId) {
        List<Course> courseMentorList = courseRepository.findByMentor(mentorId);
        List<CourseResponse> courseResponseList = new ArrayList<>();
        for(Course course : courseMentorList) {
            CourseResponse courseResponse = new CourseResponse();

            courseResponse.setCourseId(course.getId());
            courseResponse.setCourseName(course.getCourseDescription());
            courseResponse.setCompanyId(course.getCompany().getId());
            courseResponse.setCompanyName(course.getCompany().getCompanyName());
            courseResponse.setMentorId(course.getMentor().getId());
            courseResponse.setMentorName(course.getMentor().getFullName());
            courseResponse.setStartDate(course.getStartDate());
            courseResponse.setEndDate(course.getEndDate());
            courseResponse.setStatus(course.getStatus());

            courseResponseList.add(courseResponse);
        }
        GetAllCourseByMentorIdResponse getCourseByMentorIdResponse = new GetAllCourseByMentorIdResponse();
        getCourseByMentorIdResponse.setCourses(courseResponseList);

        return getCourseByMentorIdResponse;

    }


    @Override
    public GetAllCourseInCompanyResponse getAllCourseInCompanyResponse(int companyId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> courses = courseRepository.findByCompany(companyId, pageable);
        List<Course> courseList = courses.getContent();
        List<CourseResponse> courseResponseList = new ArrayList<>();
        for(Course course : courseList) {
            CourseResponse courseResponse = new CourseResponse();

            courseResponse.setCourseId(course.getId());
            courseResponse.setCourseName(course.getCourseDescription());
            courseResponse.setCompanyId(course.getCompany().getId());
            courseResponse.setCompanyName(course.getCompany().getCompanyName());
            courseResponse.setMentorId(course.getMentor().getId());
            courseResponse.setMentorName(course.getMentor().getFullName());
            courseResponse.setStartDate(course.getStartDate());
            courseResponse.setEndDate(course.getEndDate());
            courseResponse.setStatus(course.getStatus());

            courseResponseList.add(courseResponse);
        }
        GetAllCourseInCompanyResponse getAllCourseInCompanyResponse = new GetAllCourseInCompanyResponse();
        getAllCourseInCompanyResponse.setCourses(courseResponseList);
        getAllCourseInCompanyResponse.setPageNo(courses.getNumber());
        getAllCourseInCompanyResponse.setPageSize(courses.getSize());
        getAllCourseInCompanyResponse.setTotalItems(courses.getTotalElements());
        getAllCourseInCompanyResponse.setTotalPages(courses.getTotalPages());

        return getAllCourseInCompanyResponse;
    }
    @Override
    public GetAllCourseInCompanyResponse getCourseByMentorTable(int mentorId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> courses = courseRepository.findByMentorId(mentorId, pageable);
        List<Course> courseList = courses.getContent();
        List<CourseResponse> courseResponseList = new ArrayList<>();
        for(Course course : courseList) {
            CourseResponse courseResponse = new CourseResponse();

            courseResponse.setCourseId(course.getId());
            courseResponse.setCourseName(course.getCourseDescription());
            courseResponse.setCompanyId(course.getCompany().getId());
            courseResponse.setCompanyName(course.getCompany().getCompanyName());
            courseResponse.setMentorId(course.getMentor().getId());
            courseResponse.setMentorName(course.getMentor().getFullName());
            courseResponse.setStartDate(course.getStartDate());
            courseResponse.setEndDate(course.getEndDate());
            courseResponse.setStatus(course.getStatus());

            courseResponseList.add(courseResponse);
        }
        GetAllCourseInCompanyResponse getAllCourseInCompanyResponse = new GetAllCourseInCompanyResponse();
        getAllCourseInCompanyResponse.setCourses(courseResponseList);
        getAllCourseInCompanyResponse.setPageNo(courses.getNumber());
        getAllCourseInCompanyResponse.setPageSize(courses.getSize());
        getAllCourseInCompanyResponse.setTotalItems(courses.getTotalElements());
        getAllCourseInCompanyResponse.setTotalPages(courses.getTotalPages());

        return getAllCourseInCompanyResponse;
    }
    @Override
    public GetAllActivitiesInAllCourseResponse getAllTaskInAllCourse(Integer user_id, Integer company_id, int pageNo, int pageSize) {
        try {
            if (user_id == null || company_id == null) {
                throw new IllegalArgumentException("User ID and Company ID must not be null");
            }
            
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Course> courses = courseRepository.findByMentorIdAndCompanyId(user_id, company_id, pageable);
            List<Course> courseList = courses.getContent();
            List<CourseTaskResponse> courseTaskList = new ArrayList<>();
            
            for (Course course : courseList) {
                CourseTaskResponse courseTaskResponse = new CourseTaskResponse();
                courseTaskResponse.setCourseId(course.getId());
                courseTaskResponse.setCourseName(course.getCourseDescription()); 
                courseTaskResponse.setCompanyName(course.getCompany().getCompanyName());
                courseTaskResponse.setCompanyId(course.getCompany().getId());
                courseTaskResponse.setMentorName(course.getMentor().getFullName()); 
                courseTaskResponse.setTaskList(course.getTasks());
                courseTaskList.add(courseTaskResponse);
            }
            
            GetAllActivitiesInAllCourseResponse getAllActivitiesInAllCourseResponse = new GetAllActivitiesInAllCourseResponse();
            getAllActivitiesInAllCourseResponse.setCourseList(courseTaskList);
            getAllActivitiesInAllCourseResponse.setPageNo(courses.getNumber());
            getAllActivitiesInAllCourseResponse.setPageSize(courses.getSize());
            getAllActivitiesInAllCourseResponse.setTotalItems(courses.getTotalElements());
            getAllActivitiesInAllCourseResponse.setTotalPages(courses.getTotalPages());
            
            return getAllActivitiesInAllCourseResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching courses", e);
        }
    }

    @Override
    public GetCourseNameResponse getCourseNameByMentorId(int courseId ,int mentorId) {
        Course course = courseRepository.getByMentorIdAndCourseId(mentorId, courseId);
        if(course == null) {
            throw new RuntimeException("You are not enough courses");
        }
        GetCourseNameResponse getCourseNameResponse = new GetCourseNameResponse();
        getCourseNameResponse.setCourseName(course.getCourseDescription());
        return getCourseNameResponse;
    }

}
