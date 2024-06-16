package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;
import swp.internmanagement.internmanagement.payload.request.MentorFeedbackInternRequest;
import swp.internmanagement.internmanagement.payload.response.ShowAllFeedbackFromMentorResponse;

public interface MentorFeedbackInternService {

    MentorFeedbackIntern sendFeedbackIntern(MentorFeedbackInternRequest mentorFeedbackInternRequest);

    ShowAllFeedbackFromMentorResponse showAllFeedbackFromMentorResponse(int internId);
}
