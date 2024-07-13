package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseInternResponse {
    private int courseId;
    private int companyId;
    private String courseName;
    private String mentorName;
}
