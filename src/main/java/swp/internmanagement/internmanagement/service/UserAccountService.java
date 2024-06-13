package swp.internmanagement.internmanagement.service;

import org.springframework.data.domain.Pageable;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByRoleResponse;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;

import java.util.List;

public interface UserAccountService {

    GetAllUserByParamResponse getAllUserAccountsByParam(String param, int pageNo, int pageSize);

    GetUserInSameCompanyResponse getUserInSameCompany(int companyId, int pageNo, int pageSize);

    GetAllUserByRoleResponse getAllUserByRole(int roleId, String role, int pageNo, int pageSize);

    String deleteUserAccount(int userId);
}
