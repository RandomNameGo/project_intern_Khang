package swp.internmanagement.internmanagement.payload.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateTaskRequest {
    private String taskContent;
    private String startDate;
    private String endDate;
}
