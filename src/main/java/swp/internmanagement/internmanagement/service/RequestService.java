package swp.internmanagement.internmanagement.service;

import org.springframework.data.domain.Page;
import swp.internmanagement.internmanagement.entity.Request;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;

import java.util.List;

public interface RequestService {

    Request saveRequest(HelpRequest helpRequest);

    GetAllRequestResponse getRequests(int page, int size);
}
