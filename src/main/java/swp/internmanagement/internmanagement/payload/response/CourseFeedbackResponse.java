package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseFeedbackResponse {
    private int internId;
    private String internName;
    private String feedbackContent;
}
