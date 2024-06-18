package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
}