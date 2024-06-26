package swp.internmanagement.internmanagement.payload.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternResponse {
    private int internId;
    private String internName;
    private String email;
}
