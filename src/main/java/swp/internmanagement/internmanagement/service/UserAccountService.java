package swp.internmanagement.internmanagement.service;

<<<<<<< HEAD
import swp.internmanagement.internmanagement.models.UserAccount;
=======
import org.springframework.data.domain.Pageable;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;
>>>>>>> f2b7d4be666df02ff64426c9997d43281f5ac87d

import java.util.List;

public interface UserAccountService {

<<<<<<< HEAD
    List<UserAccount> getAllUserAccountsByParam(String param);
=======
    GetAllUserByParamResponse getAllUserAccountsByParam(String param, int pageNo, int pageSize);

    GetUserInSameCompanyResponse getUserInSameCompany(int companyId, int pageNo, int pageSize);
>>>>>>> f2b7d4be666df02ff64426c9997d43281f5ac87d
}
