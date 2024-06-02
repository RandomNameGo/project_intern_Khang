package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.models.User_account;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserRepository userAccountRepository;


    @Override
    public List<User_account> getAllUserAccountsByParam(String param) {
        return userAccountRepository.findUserAccountByParam(param);
    }
}
