package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.MentorFeedbackIntern;
import swp.internmanagement.internmanagement.payload.request.MentorFeedbackInternRequest;

public interface MentorFeedbackInternService {

    MentorFeedbackIntern sendFeedbackIntern(MentorFeedbackInternRequest mentorFeedbackInternRequest);

}
