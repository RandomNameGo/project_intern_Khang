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
    @Query("SELECT u from UserAccount u where u.company.companyName like %?1% or u.role like %?1% and u.role != 'ROLE_ADMIN' ")
    Page<UserAccount> findUserAccountByParam(String param, Pageable pageable);

    //Coordinator search intern and mentor in same company
    @Query("select u from UserAccount u where u.role = 'ROLE_MENTOR' or u.role = 'ROLE_INTERN' and u.company.id = ?1")
    Page<UserAccount> findAllUsersInCompany(int companyId, Pageable pageable);

    @Query("select u from UserAccount u where u.company.id = ?1 and u.role = ?2")
    Page<UserAccount> findAllMemberInCompany(int companyId, String role, Pageable pageable);

}
