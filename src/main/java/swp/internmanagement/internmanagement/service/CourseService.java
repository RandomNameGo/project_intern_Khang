package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllActivitiesInAllCourseResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllCourseByMentorIdResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllCourseInCompanyResponse;
import swp.internmanagement.internmanagement.payload.response.GetCourseNameResponse;

import java.util.List;

public interface CourseService {

    Course addCourse(CreateCourseRequest createCourseRequest, int companyId);

    Course getCourse(int courseId);

    GetCourseNameResponse getCourseName(int courseId, int internId);

    String deleteCourse(int courseId);

    void updateCourseStatus();

    GetAllCourseByMentorIdResponse getCourseByMentor(int mentorId);

    GetAllCourseInCompanyResponse getAllCourseInCompanyResponse(int companyId, int pageNo, int pageSize);

    GetAllCourseInCompanyResponse getCourseByMentorTable(int mentorId, int pageNo, int pageSize);

    GetAllActivitiesInAllCourseResponse getAllTaskInAllCourse(Integer user_id, Integer company_id, int pageNo, int pageSize);

    GetCourseNameResponse getCourseNameByMentorId(int courseId, int mentorId);

    List<Course> getAllEndCourses(int companyId);
}
