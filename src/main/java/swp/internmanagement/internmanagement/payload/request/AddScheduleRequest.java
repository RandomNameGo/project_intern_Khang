package swp.internmanagement.internmanagement.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddScheduleRequest {
    private String time;
    private List<ApplicationIdRequest> applicationId;
    private String location;
}
