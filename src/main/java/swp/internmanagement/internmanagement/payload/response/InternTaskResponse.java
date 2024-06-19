package swp.internmanagement.internmanagement.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class InternTaskResponse {
    private int taskId;
    private int courseId;
    private String taskContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean status;
}
