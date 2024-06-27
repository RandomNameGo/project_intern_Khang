package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.internmanagement.internmanagement.entity.CoordinatorFeedbackIntern;
import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;

import java.util.List;

public interface CoordinatorFeedbackInternRepository extends JpaRepository<CoordinatorFeedbackIntern, Integer> {

    List<CoordinatorFeedbackIntern> findByInternId(int internId);

    List<CoordinatorFeedbackIntern> findByCoordinatorId(int internId);
}