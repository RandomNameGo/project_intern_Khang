package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Request;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;

public interface RequestService {

    Request saveRequest(HelpRequest helpRequest);

    GetAllRequestResponse getRequests(int page, int size);
}
