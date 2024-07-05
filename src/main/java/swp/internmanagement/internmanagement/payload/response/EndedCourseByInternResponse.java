package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EndedCourseByInternResponse {
    private int companyId;
    private String companyName;
    private int courseId;
    private String courseName;
    private int mentorId;
    private String mentorName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
    public boolean feedback;
}
