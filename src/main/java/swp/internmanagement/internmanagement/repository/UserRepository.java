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
    @Query("SELECT u from UserAccount u where u.role != 'ROLE_ADMIN' and u.status is not null")
    Page<UserAccount> findUserAccountByParam(Pageable pageable);

    //Coordinator search intern and mentor in same company
    @Query("select u from UserAccount u where u.role = 'ROLE_MENTOR' or u.role = 'ROLE_INTERN' and u.company.id = ?1 and u.status is not null")
    Page<UserAccount> findAllUsersInCompany(Integer companyId, Pageable pageable);

    @Query("select u from UserAccount u where u.company.id = ?1 and u.role = ?2 and u.status is not null")
    Page<UserAccount> findAllMemberInCompany(Integer companyId, String role, Pageable pageable);

    Optional<UserAccount> findByVerificationCodeAndUserName(String verificationCode, String userName);

    @Query("select u from UserAccount u where u.company.id = ?1 and u.role = ?2 and u.status is not null")
    List<UserAccount> findAllUserByRole(Integer companyId, String role);

    @Query("SELECT u from UserAccount u where u.role != 'ROLE_ADMIN' AND u.status = 1 and u.status is not null")
    Page<UserAccount> findAllUserAccount(Pageable pageable);

    @Query("select u from UserAccount u where u.role = 'ROLE_MENTOR' and u.company.id = :companyId and u.status is not null")
    List<UserAccount> findAllMentorByCompanyId(Integer companyId);

    @Query("select u from UserAccount u where u.role = 'ROLE_INTERN' and u.company.id = :companyId and u.status is not null")
    List<UserAccount> findAllInternByCompanyId(Integer companyId);

    @Query("select u from UserAccount u where u.company.id = :companyId and u.id != :userId and u.status is not null")
    Page<UserAccount> findAllByCompanyId(Integer companyId, Integer userId, Pageable pageable);

    @Query("select u from UserAccount u where u.company.id = :companyId and u.role = :role and u.id != :userId and u.status is not null")
    Page<UserAccount> findAllByRoleInCompany(Integer companyId, Integer userId, String role, Pageable pageable);
}
