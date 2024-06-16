package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.MentorFeedbackInternRequest;
import swp.internmanagement.internmanagement.payload.response.FeedbackResponse;
import swp.internmanagement.internmanagement.payload.response.ShowAllFeedbackFromMentorResponse;
import swp.internmanagement.internmanagement.repository.MentorFeedbackInternRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ShowAllFeedbackFromMentorResponse showAllFeedbackFromMentorResponse(int internId) {
        if(!userAccountRepository.existsById(internId)) {
            return null;
        }
        List<MentorFeedbackIntern> mentorFeedbackInternList = repository.findByInternId(internId);
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        for(MentorFeedbackIntern mentorFeedbackIntern : mentorFeedbackInternList) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setMentorName(mentorFeedbackIntern.getMentor().getFullName());
            feedbackResponse.setContent(mentorFeedbackIntern.getFeedbackContent());
            feedbackResponseList.add(feedbackResponse);
        }
        ShowAllFeedbackFromMentorResponse response = new ShowAllFeedbackFromMentorResponse();
        response.setFeedbackResponses(feedbackResponseList);
        return response;
    }
}
