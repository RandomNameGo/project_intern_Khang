package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Request;
import swp.internmanagement.internmanagement.entity.Task;

import java.util.List;

@Getter
@Setter
public class GetAllTaskInCourseResponse {
    private List<Task> tasks;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
