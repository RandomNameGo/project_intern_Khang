package swp.internmanagement.internmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import swp.internmanagement.internmanagement.models.UserAccount;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Integer> {
    Optional<UserAccount> findByUserName(String userName); // Corrected method name
    Boolean existsByUserName(String userName); // Corrected method name
    Boolean existsByEmail(String email);
    @Query(value = "SELECT TOP 1 user_id FROM User_account ORDER BY user_id DESC", nativeQuery = true)
    int findLastUserId();

    @Query("SELECT u from UserAccount u where u.company.companyName like %?1% or u.role like %?1%")
    List<UserAccount> findUserAccountByParam(String param);
}
