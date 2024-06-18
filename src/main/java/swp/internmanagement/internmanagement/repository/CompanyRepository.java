package swp.internmanagement.internmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import swp.internmanagement.internmanagement.entity.Company;


public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByCompanyName(String companyName);
}