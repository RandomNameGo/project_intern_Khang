package swp.internmanagement.internmanagement.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationIdRequest {
    @JsonProperty("applicationId")
    private int applicationId;
}
