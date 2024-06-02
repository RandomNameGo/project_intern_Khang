package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.Field;

public interface FieldRepository extends JpaRepository<Field, Integer> {
}