package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetListInternResultFromCourseResponse {
    List<GetInternResultFromCourseResponse> getListInternResultFromCourse;
}
