package swp.internmanagement.internmanagement.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GetAllJobRes {
    private List<jobRes> jobs;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
