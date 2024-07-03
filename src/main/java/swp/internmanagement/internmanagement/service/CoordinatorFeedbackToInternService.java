package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.payload.request.FeedBackRequest;
import swp.internmanagement.internmanagement.payload.response.ShowAllFeedbackResponse;

public interface CoordinatorFeedbackToInternService {

    String sendFeedback(FeedBackRequest feedBackRequest, int coordinatorId, int internId);

    ShowAllFeedbackResponse getFeedback(int internId);
}
