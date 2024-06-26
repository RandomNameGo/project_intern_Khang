package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllInternInCourseResponse;
import swp.internmanagement.internmanagement.payload.response.GetListInternResultFromCourseResponse;

import java.util.List;

public interface CourseInternService {

    String addInternToCourse(List<AddInternToCourseRequest> request, int courseId);

    List<CourseIntern> geCoursesByInternId(int internId);

    void updateResult(int internId, int courseId);

    GetListInternResultFromCourseResponse getListInternResultFromCourse(int courseId, int mentorId);

    GetAllInternInCourseResponse getAllInternInCourse(int courseId, int mentorId);

    GetAllInternInCourseResponse getAllInternInCourseByCoordinator(int courseId, int coordinatorId);
}
