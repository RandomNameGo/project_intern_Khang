package swp.internmanagement.internmanagement.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CreateCourseRequest {
    private int mentorId;
    private String courseDescription;
    private LocalDate startDate;
    private LocalDate endDate;
}
