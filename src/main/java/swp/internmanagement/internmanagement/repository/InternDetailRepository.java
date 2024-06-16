package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.InternDetail;

public interface InternDetailRepository extends JpaRepository<InternDetail, Integer> {
}