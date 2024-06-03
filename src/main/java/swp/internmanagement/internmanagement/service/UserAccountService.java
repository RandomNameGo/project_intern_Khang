package swp.internmanagement.internmanagement.service;

import org.springframework.data.domain.Pageable;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;

import java.util.List;

public interface UserAccountService {

    List<UserAccount> getAllUserAccountsByParam(String param);

    GetUserInSameCompanyResponse getUserInSameCompany(int companyId, int pageNo, int pageSize);
}
