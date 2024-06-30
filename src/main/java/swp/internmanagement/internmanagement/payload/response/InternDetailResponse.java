package swp.internmanagement.internmanagement.payload.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternDetailResponse {
    private Integer internId;
    private String internName;
    private String workHistory;
    private String educationBackground;
}
