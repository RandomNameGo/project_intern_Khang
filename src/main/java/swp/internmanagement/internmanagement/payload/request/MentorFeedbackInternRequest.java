package swp.internmanagement.internmanagement.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorFeedbackInternRequest {
    private int mentorId;
    private int internId;
    private String feedbackContent;
}
