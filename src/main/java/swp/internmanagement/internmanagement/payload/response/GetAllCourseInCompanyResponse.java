package swp.internmanagement.internmanagement.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetAllCourseInCompanyResponse {
    private List<CourseResponse> courses;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
