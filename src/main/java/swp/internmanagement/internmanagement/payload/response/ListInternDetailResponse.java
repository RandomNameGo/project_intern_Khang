package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListInternDetailResponse {
    private List<InternDetailResponse> internDetailResponseLists;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
