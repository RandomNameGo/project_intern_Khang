package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;
import swp.internmanagement.internmanagement.payload.request.FeedBackRequest;
import swp.internmanagement.internmanagement.payload.request.MentorFeedbackInternRequest;
import swp.internmanagement.internmanagement.payload.response.ShowAllFeedbackResponse;

public interface MentorFeedbackInternService {

    String sendFeedbackIntern(FeedBackRequest feedBackRequest, int mentorId, int internId);

    ShowAllFeedbackResponse showAllFeedbackFromMentorResponse(int internId);
}
