package swp.internmanagement.internmanagement.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Request;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;
import swp.internmanagement.internmanagement.repository.RequestRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;


@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userAccountRepository;

    @Override
    public Request saveRequest(HelpRequest helpRequest) {
        Request request = new Request();
        UserAccount userAccount = new UserAccount();
        int userId = helpRequest.getSenderId();
        userAccount = userAccountRepository.findById(userId).get();
        request.setUser(userAccount);
        request.setRequestType(helpRequest.getHelpType());
        request.setRequestContent(helpRequest.getHelpContent());
        requestRepository.save(request);
        return request;
    }

    @Override
    public GetAllRequestResponse getRequests(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Request> requests = requestRepository.findAll(pageable);
        List<Request> requestList = requests.getContent();

        GetAllRequestResponse response = new GetAllRequestResponse();
        response.setRequests(requestList);
        response.setPageNo(requests.getNumber());
        response.setPageSize(requests.getSize());
        response.setTotalItems(requests.getTotalElements());
        response.setTotalPages(requests.getTotalPages());
        return response;
    }


}
