package swp.internmanagement.internmanagement.service;


import swp.internmanagement.internmanagement.payload.request.SendCourseFeedbackRequest;
import swp.internmanagement.internmanagement.payload.response.CourseFeedbackResponse;

import java.util.List;

public interface CourseFeedbackService {
    String sendCourseFeedback(SendCourseFeedbackRequest sendCourseFeedbackRequest, int internId, int courseId);

    Boolean verifyCourseFeedback(int internId, int courseId);

    List<CourseFeedbackResponse> getAllCourseFeedback(int courseId, int coordinatorId);
}
