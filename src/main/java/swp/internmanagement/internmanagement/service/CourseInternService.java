package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;

import java.util.List;

public interface CourseInternService {

    String addInternToCourse(AddInternToCourseRequest request, int courseId);

    List<CourseIntern> geCoursesByInternId(int internId);


}
