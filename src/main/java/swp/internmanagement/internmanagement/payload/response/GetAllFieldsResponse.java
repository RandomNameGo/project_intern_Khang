package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.entity.Field;

import java.util.List;

@Getter
@Setter
public class GetAllFieldsResponse {
    private List<Field> fields;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
