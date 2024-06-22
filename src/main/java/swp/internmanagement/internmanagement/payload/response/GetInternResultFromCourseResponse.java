package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetInternResultFromCourseResponse {
    private int internId;
    private String internName;
    private long totalTask;
    private long completedTasks;
    private double result;
}
