package swp.internmanagement.internmanagement.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Request;
import swp.internmanagement.internmanagement.models.User_account;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;
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
        User_account userAccount = new User_account();
        int userId = helpRequest.getSenderId();
        userAccount = userAccountRepository.findById(userId).get();
        request.setUser(userAccount);
        request.setRequestType(helpRequest.getHelpType());
        request.setRequestContent(helpRequest.getHelpContent());
        requestRepository.save(request);
        return request;
    }

    @Override
    public List<Request> getRequests() {
        return requestRepository.findAll();
    }


}
