package swp.internmanagement.internmanagement.payload.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class HelpRequest {
    private int senderId;
    private String helpType;
    private String helpContent;
}
