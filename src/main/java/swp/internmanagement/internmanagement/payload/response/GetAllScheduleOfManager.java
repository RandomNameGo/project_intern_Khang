package swp.internmanagement.internmanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllScheduleOfManager {
    private String title;
    private String start;
    private String end;
    private String description;
}
