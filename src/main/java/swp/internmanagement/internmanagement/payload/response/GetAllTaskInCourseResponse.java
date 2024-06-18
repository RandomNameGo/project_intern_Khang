package swp.internmanagement.internmanagement.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Task;

@Getter
@Setter
public class GetAllTaskInCourseResponse {
    private List<Task> tasks;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
