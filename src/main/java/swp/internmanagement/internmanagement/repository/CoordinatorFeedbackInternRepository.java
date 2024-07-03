package swp.internmanagement.internmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import swp.internmanagement.internmanagement.entity.CoordinatorFeedbackIntern;

public interface CoordinatorFeedbackInternRepository extends JpaRepository<CoordinatorFeedbackIntern, Integer> {

    List<CoordinatorFeedbackIntern> findByInternId(int internId);

    List<CoordinatorFeedbackIntern> findByCoordinatorId(int internId);
}