package swp.internmanagement.internmanagement.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddScheduleRequest {
    private String time;
    private int[] applicationId;
    private String location;
}
