package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.CoordinatorFeedbackIntern;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.FeedBackRequest;
import swp.internmanagement.internmanagement.payload.response.FeedbackResponse;
import swp.internmanagement.internmanagement.payload.response.ShowAllFeedbackResponse;
import swp.internmanagement.internmanagement.repository.CoordinatorFeedbackInternRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoordinatorFeedbackToInternServiceImpl implements CoordinatorFeedbackToInternService {

    @Autowired
    private CoordinatorFeedbackInternRepository coordinatorFeedbackInternRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String sendFeedback(FeedBackRequest feedBackRequest, int coordinatorId, int internId) {
        CoordinatorFeedbackIntern coordinatorFeedbackIntern = new CoordinatorFeedbackIntern();

        UserAccount coordinator = userRepository.findById(coordinatorId).get();
        UserAccount intern = userRepository.findById(internId).get();

        coordinatorFeedbackIntern.setCoordinator(coordinator);
        coordinatorFeedbackIntern.setIntern(intern);
        coordinatorFeedbackIntern.setFeedbackContent(feedBackRequest.getFeedback());
        coordinatorFeedbackInternRepository.save(coordinatorFeedbackIntern);

        return "Sent Feedback Successfully";
    }

    @Override
    public ShowAllFeedbackResponse getFeedback(int internId) {

        if(!userRepository.existsById(internId)){
            throw new RuntimeException("User not found");
        }
        List<CoordinatorFeedbackIntern> feedbackInternList = coordinatorFeedbackInternRepository.findByInternId(internId);
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        for (CoordinatorFeedbackIntern feedbackIntern : feedbackInternList) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setSenderName(feedbackIntern.getCoordinator().getFullName());
            feedbackResponse.setContent(feedbackIntern.getFeedbackContent());
            feedbackResponseList.add(feedbackResponse);
        }

        ShowAllFeedbackResponse showAllFeedbackResponse = new ShowAllFeedbackResponse();
        showAllFeedbackResponse.setFeedbackResponses(feedbackResponseList);

        return showAllFeedbackResponse;
    }
}
