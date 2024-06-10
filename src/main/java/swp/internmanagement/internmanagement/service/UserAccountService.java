package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.payload.response.GetAllUserByRoleResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;

public interface UserAccountService {

    GetAllUserByParamResponse getAllUserAccountsByParam(String param, int pageNo, int pageSize);

    GetUserInSameCompanyResponse getUserInSameCompany(int companyId, int pageNo, int pageSize);

    GetAllUserByRoleResponse getAllMentor(int companyId, String role, int pageNo, int pageSize);

}
