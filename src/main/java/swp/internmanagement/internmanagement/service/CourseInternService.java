package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;

public interface CourseInternService {

    String addInternToCourse(AddInternToCourseRequest request, int courseId);

}
