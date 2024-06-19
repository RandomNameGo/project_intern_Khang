package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Course;
import java.util.List;


@Getter
@Setter
public class GetAllCourseInCompanyResponse {
    private List<CourseResponse> courses;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
