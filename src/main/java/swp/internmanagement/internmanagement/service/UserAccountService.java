package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.models.User_account;

import java.util.List;

public interface UserAccountService {

    List<User_account> getAllUserAccountsByParam(String param);
}
