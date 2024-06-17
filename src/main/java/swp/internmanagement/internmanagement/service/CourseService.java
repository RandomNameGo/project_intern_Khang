package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.response.GetCourseNameResponse;

public interface CourseService {

    Course addCourse(CreateCourseRequest createCourseRequest, int companyId);

    Course getCourse(int courseId);

    GetCourseNameResponse getCourseName(int courseId);

    void updateCourseStatus(Course course);

}
