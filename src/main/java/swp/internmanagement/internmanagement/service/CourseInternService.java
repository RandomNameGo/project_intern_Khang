package swp.internmanagement.internmanagement.service;

import java.util.List;

import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.payload.response.CourseInternResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllInternInCourseResponse;
import swp.internmanagement.internmanagement.payload.response.GetListInternResultFromCourseResponse;
import swp.internmanagement.internmanagement.payload.response.ListEndedCourseByInternResponse;

public interface CourseInternService {

    String addInternToCourse(List<AddInternToCourseRequest> request, int courseId);

    List<CourseInternResponse> geCoursesByInternId(int internId);

    void updateResult(int internId, int courseId);

    GetListInternResultFromCourseResponse getListInternResultFromCourse(int courseId, int mentorId);

    GetAllInternInCourseResponse getAllInternInCourse(int courseId, int mentorId);

    GetAllInternInCourseResponse getAllInternInCourseByCoordinator(int courseId, int coordinatorId);

    Boolean verifyCourseIntern(int internId, int courseId);

    ListEndedCourseByInternResponse getListEndedCourseByIntern(int internId);
}
