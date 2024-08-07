package swp.internmanagement.internmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import swp.internmanagement.internmanagement.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
   
    @Query("SELECT s FROM Schedule s WHERE s.application.job.company.id =:id")
    List<Schedule> findScheduleByCompanyId(Integer id);
    
    @Query("SELECT s FROM Schedule s WHERE s.application.id = :jobApplicationId")
    Schedule findByJobApplicationId(Integer jobApplicationId);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.application.id = :jobApplicationId")
    void deleteByJobApplicationId(Integer jobApplicationId);

    @Query("SELECT s FROM Schedule s WHERE s.application.job.company.id =:id")
    Page<Schedule> findByCompanyId(Integer id, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.id = :scheduleId")
    void deleteScheduleById(Integer scheduleId);
}