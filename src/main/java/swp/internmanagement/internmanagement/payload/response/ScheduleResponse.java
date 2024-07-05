package swp.internmanagement.internmanagement.payload.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleResponse {
    private int id;
    private String title;
    private int applicationId;
    private String name;
    private String start;
    private String end;
    private String description;
}
