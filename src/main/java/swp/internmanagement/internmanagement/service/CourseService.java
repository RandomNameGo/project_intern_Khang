package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;

public interface CourseService {

    Course addCourse(CreateCourseRequest createCourseRequest, int companyId);

    Course getCourse(int courseId);
}
