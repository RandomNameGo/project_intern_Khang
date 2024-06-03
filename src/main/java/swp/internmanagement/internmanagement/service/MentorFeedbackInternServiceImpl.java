package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.MentorFeedbackInternRequest;
import swp.internmanagement.internmanagement.repository.MentorFeedbackInternRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

@Service
public class MentorFeedbackInternServiceImpl implements MentorFeedbackInternService{

    @Autowired
    private MentorFeedbackInternRepository repository;

    @Autowired
    private UserRepository userAccountRepository;

    @Override
    public MentorFeedbackIntern sendFeedbackIntern(MentorFeedbackInternRequest mentorFeedbackInternRequest) {
        int mentorId = mentorFeedbackInternRequest.getMentorId();
        int internId = mentorFeedbackInternRequest.getInternId();
        UserAccount mentor = userAccountRepository.findById(mentorId).get();
        UserAccount intern = userAccountRepository.findById(internId).get();
        MentorFeedbackIntern mentorFeedbackIntern = new MentorFeedbackIntern();
        mentorFeedbackIntern.setMentor(mentor);
        mentorFeedbackIntern.setIntern(intern);
        mentorFeedbackIntern.setFeedbackContent(mentorFeedbackInternRequest.getFeedbackContent());
        return repository.save(mentorFeedbackIntern);
    }
}
