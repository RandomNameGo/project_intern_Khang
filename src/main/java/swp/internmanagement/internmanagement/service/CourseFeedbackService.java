package swp.internmanagement.internmanagement.service;


import swp.internmanagement.internmanagement.payload.request.SendCourseFeedbackRequest;

public interface CourseFeedbackService {
    String sendCourseFeedback(SendCourseFeedbackRequest sendCourseFeedbackRequest, int internId, int courseId);
}
