package swp.internmanagement.internmanagement.payload.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInternToCourseRequest {
    private int courseId;
    private int[] internId;
}
