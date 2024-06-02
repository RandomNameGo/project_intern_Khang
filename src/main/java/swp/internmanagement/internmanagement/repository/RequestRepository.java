package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}