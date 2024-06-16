package swp.internmanagement.internmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;

import java.util.List;

public interface MentorFeedbackInternRepository extends JpaRepository<MentorFeedbackIntern, Integer> {

    List<MentorFeedbackIntern> findByInternId(int internId);
}