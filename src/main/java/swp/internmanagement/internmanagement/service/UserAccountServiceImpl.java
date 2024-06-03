package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.models.UserAccount;
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
}
