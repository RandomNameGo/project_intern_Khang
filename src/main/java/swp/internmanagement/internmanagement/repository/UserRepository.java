package swp.internmanagement.internmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("SELECT u from UserAccount u where u.role != 'ROLE_ADMIN' ")
    Page<UserAccount> findUserAccountByParam(Pageable pageable);

    //Coordinator search intern and mentor in same company
    @Query("select u from UserAccount u where u.role = 'ROLE_MENTOR' or u.role = 'ROLE_INTERN' and u.company.id = ?1")
    Page<UserAccount> findAllUsersInCompany(int companyId, Pageable pageable);

    @Query("select u from UserAccount u where u.company.id = ?1 and u.role = ?2")
    Page<UserAccount> findAllMemberInCompany(int companyId, String role, Pageable pageable);

    Optional<UserAccount> findByVerificationCodeAndUserName(String verificationCode, String userName);

    @Query("select u from UserAccount u where u.company.id = ?1 and u.role = ?2")
    List<UserAccount> findAllUserByRole(int companyId, String role);

    @Query("SELECT u from UserAccount u where u.role != 'ROLE_ADMIN' ")
    Page<UserAccount> findAllUserAccount(Pageable pageable);

    @Query("select u from UserAccount u where u.role = 'ROLE_MENTOR' and u.company.id = :companyId ")
    List<UserAccount> findAllMentorByCompanyId(int companyId);

}
