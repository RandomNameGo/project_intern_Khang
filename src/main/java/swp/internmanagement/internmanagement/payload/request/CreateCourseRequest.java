package swp.internmanagement.internmanagement.payload.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateCourseRequest {
    private int mentorId;
    private String courseDescription;
    private String startDate;
    private String endDate;
}
