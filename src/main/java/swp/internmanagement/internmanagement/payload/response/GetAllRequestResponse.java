package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Request;

import java.util.List;

@Getter
@Setter
public class GetAllRequestResponse {
    private List<Request> requests;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;

}
