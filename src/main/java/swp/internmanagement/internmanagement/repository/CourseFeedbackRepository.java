package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.CourseFeedback;
import swp.internmanagement.internmanagement.entity.CourseFeedbackId;

public interface CourseFeedbackRepository extends JpaRepository<CourseFeedback, CourseFeedbackId> {
}