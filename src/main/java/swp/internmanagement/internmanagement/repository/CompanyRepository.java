package swp.internmanagement.internmanagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import swp.internmanagement.internmanagement.entity.Company;


public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByCompanyName(String companyName);

    @Query("SELECT c FROM Company c WHERE c.id != 1")
    Page<Company> getAllCompany(Pageable pageable);
}