package swp.internmanagement.internmanagement.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CreateTaskRequest {
    private String taskContent;
    private LocalDate startDate;
    private LocalDate endDate;
}
