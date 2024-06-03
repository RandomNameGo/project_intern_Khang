package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.models.UserAccount;

import java.util.List;

public interface UserAccountService {

    List<UserAccount> getAllUserAccountsByParam(String param);
}
