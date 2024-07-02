package swp.internmanagement.internmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendHelpRequest {
    private int senderId;
    private String helpType;
    private String description;
    private String companyEmail;
    private String companyName;
    private String companyDescription;
}
