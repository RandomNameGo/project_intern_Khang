package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByRoleResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserRepository userAccountRepository;


    //This method is for admin
    @Override
    public GetAllUserByParamResponse getAllUserAccountsByParam(String param, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findUserAccountByParam(param, pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        GetAllUserByParamResponse getAllUserByParamResponse = new GetAllUserByParamResponse();
        getAllUserByParamResponse.setUserList(userAccountList);
        getAllUserByParamResponse.setPageNo(userAccounts.getNumber());
        getAllUserByParamResponse.setPageSize(userAccounts.getSize());
        getAllUserByParamResponse.setTotalItems(userAccounts.getTotalElements());
        getAllUserByParamResponse.setTotalPages(userAccounts.getTotalPages());

        return getAllUserByParamResponse;
    }

    //This method is for coordinator
    @Override
    public GetUserInSameCompanyResponse getUserInSameCompany(int companyId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findAllUsersInCompany(companyId, pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        GetUserInSameCompanyResponse getUserInSameCompanyResponse = new GetUserInSameCompanyResponse();
        getUserInSameCompanyResponse.setUserList(userAccountList);
        getUserInSameCompanyResponse.setPageNo(userAccounts.getNumber());
        getUserInSameCompanyResponse.setPageSize(userAccounts.getSize());
        getUserInSameCompanyResponse.setTotalItems(userAccounts.getTotalElements());
        getUserInSameCompanyResponse.setTotalPages(userAccounts.getTotalPages());

        return getUserInSameCompanyResponse;
    }

    @Override
    public GetAllUserByRoleResponse getAllMentor(int companyId, String role, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findAllMemberInCompany(companyId, role, pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        GetAllUserByRoleResponse getAllMentorResponse = new GetAllUserByRoleResponse();
        getAllMentorResponse.setUserAccountList(userAccountList);
        getAllMentorResponse.setPageNo(userAccounts.getNumber());
        getAllMentorResponse.setPageSize(userAccounts.getSize());
        getAllMentorResponse.setTotalItems(userAccounts.getTotalElements());
        getAllMentorResponse.setTotalPages(userAccounts.getTotalPages());

        return getAllMentorResponse;
    }


}
