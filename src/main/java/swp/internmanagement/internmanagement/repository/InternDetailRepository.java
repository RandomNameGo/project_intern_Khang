package swp.internmanagement.internmanagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swp.internmanagement.internmanagement.entity.InternDetail;

public interface InternDetailRepository extends JpaRepository<InternDetail, Integer> {

    @Query("select i from InternDetail i where i.user.id = :internId")
    InternDetail findByInternId(Integer internId);

    @Query("SELECT i FROM InternDetail i WHERE i.user.id = :userId")
    Optional<InternDetail> findByUserId(@Param("userId") Integer userId);

    @Query("select i from InternDetail i where i.user.company.id = :companyId")
    Page<InternDetail> findByCompanyId(Integer companyId, Pageable pageable);
}