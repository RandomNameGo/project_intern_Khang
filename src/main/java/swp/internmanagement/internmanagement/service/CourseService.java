package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllCourseInCompanyResponse;
import swp.internmanagement.internmanagement.payload.response.GetCourseNameResponse;

import java.util.List;

public interface CourseService {

    Course addCourse(CreateCourseRequest createCourseRequest, int companyId);

    Course getCourse(int courseId);

    GetCourseNameResponse getCourseName(int courseId);

    String deleteCourse(int courseId);

    void updateCourseStatus();

    List<Course> getCourseByMentor(int mentorId);

    GetAllCourseInCompanyResponse getAllCourseInCompanyResponse(int companyId, int pageNo, int pageSize);
}
