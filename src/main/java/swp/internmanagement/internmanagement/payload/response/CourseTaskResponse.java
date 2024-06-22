package swp.internmanagement.internmanagement.payload.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Task;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseTaskResponse {
    private int courseId;
    private String courseName;
    private int companyId;
    private String companyName;
    private String mentorName;
    private List<Task> taskList;
}
