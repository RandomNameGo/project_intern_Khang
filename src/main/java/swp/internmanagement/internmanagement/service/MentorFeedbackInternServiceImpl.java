package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.FeedBackRequest;
import swp.internmanagement.internmanagement.payload.response.FeedbackResponse;
import swp.internmanagement.internmanagement.payload.response.ShowAllFeedbackResponse;
import swp.internmanagement.internmanagement.repository.MentorFeedbackInternRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MentorFeedbackInternServiceImpl implements MentorFeedbackInternService{

    @Autowired
    private MentorFeedbackInternRepository repository;

    @Autowired
    private UserRepository userAccountRepository;


    @Override
    public String sendFeedbackIntern(FeedBackRequest feedBackRequest, int mentorId, int internId) {
        MentorFeedbackIntern mentorFeedbackIntern = new MentorFeedbackIntern();

        UserAccount mentor= userAccountRepository.findById(mentorId).get();
        UserAccount intern = userAccountRepository.findById(internId).get();

        if (!Objects.equals(mentor.getCompany().getId(), intern.getCompany().getId())) {
            throw new RuntimeException("Mentor's company does not belong to this company");
        }

        if(!userAccountRepository.existsById(mentorId) || !userAccountRepository.existsById(internId)){
            throw new RuntimeException("Mentor or intern is not found");
        }

        mentorFeedbackIntern.setMentor(mentor);
        mentorFeedbackIntern.setIntern(intern);
        mentorFeedbackIntern.setFeedbackContent(feedBackRequest.getFeedback());
        repository.save(mentorFeedbackIntern);

        return "Sent Feedback Successfully";
    }

    @Override
    public ShowAllFeedbackResponse showAllFeedbackFromMentorResponse(int internId) {
        if(!userAccountRepository.existsById(internId)) {
            throw new RuntimeException("Intern is not found");
        }
        List<MentorFeedbackIntern> mentorFeedbackInternList = repository.findByInternId(internId);
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        for(MentorFeedbackIntern mentorFeedbackIntern : mentorFeedbackInternList) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setSenderName(mentorFeedbackIntern.getMentor().getFullName());
            feedbackResponse.setContent(mentorFeedbackIntern.getFeedbackContent());
            feedbackResponseList.add(feedbackResponse);
        }
        ShowAllFeedbackResponse response = new ShowAllFeedbackResponse();
        response.setFeedbackResponses(feedbackResponseList);
        return response;
    }
}
