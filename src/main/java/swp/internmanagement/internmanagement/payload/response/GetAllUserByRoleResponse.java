package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;
import swp.internmanagement.internmanagement.models.UserAccount;

import java.util.List;

@Getter
@Setter
public class GetAllUserByRoleResponse {
    private List<UserAccount> userAccountList;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
