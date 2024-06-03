package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserRepository userAccountRepository;


    @Override
    public List<UserAccount> getAllUserAccountsByParam(String param) {
        return userAccountRepository.findUserAccountByParam(param);
    }

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
}
