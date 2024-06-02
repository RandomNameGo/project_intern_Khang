package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Request;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;

import java.util.List;

public interface RequestService {

    Request saveRequest(HelpRequest helpRequest);

    List<Request> getRequests();
}
