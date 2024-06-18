package swp.internmanagement.internmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;

public interface MentorFeedbackInternRepository extends JpaRepository<MentorFeedbackIntern, Integer> {

    List<MentorFeedbackIntern> findByInternId(int internId);
}