package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}