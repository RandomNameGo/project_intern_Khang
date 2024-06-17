package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.SignupRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByRoleResponse;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;

import java.util.List;

public interface UserAccountService {

    GetAllUserByParamResponse getAllUserAccountsByParam(String param, int pageNo, int pageSize);

    GetUserInSameCompanyResponse getUserInSameCompany(int companyId, int pageNo, int pageSize);

    GetAllUserByRoleResponse getAllUserByRole(int companyId, String role, int pageNo, int pageSize);

    String deleteUserAccount(int userId);

    boolean RegisterUser(List<SignupRequest> listSignUpRequest);

    boolean verifyAndActivate(String code, String userName, String password);

    List<UserAccount> getAllUserAccountByRole(int companyId, String role);
}
