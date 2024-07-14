package swp.internmanagement.internmanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Request;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;
import swp.internmanagement.internmanagement.payload.request.SendHelpRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;
import swp.internmanagement.internmanagement.repository.RequestRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userAccountRepository;

    @Autowired
    private EmailService emailService;
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
        Page<Request> requests = requestRepository.findAllRequest(pageable);
        List<Request> requestList = requests.getContent();

        GetAllRequestResponse response = new GetAllRequestResponse();
        response.setRequests(requestList);
        response.setPageNo(requests.getNumber());
        response.setPageSize(requests.getSize());
        response.setTotalItems(requests.getTotalElements());
        response.setTotalPages(requests.getTotalPages());
        return response;
    }

    public String editFormat(String title, String content){
        String tilteInDb = "<strong>"+title+"</strong><br/>";
        String contentInDb = "<p>"+content+"</p>";
        String result = tilteInDb+contentInDb;
        return result;
    }

    @Override
    public SendHelpRequest saveHelpRequest(SendHelpRequest sendHelpRequest) {
        Request help = new Request();
        if(sendHelpRequest.getHelpType().equals("Other")){
            UserAccount user =new UserAccount();
            user.setId(sendHelpRequest.getSenderId());
            String contentReq = editFormat("Description", sendHelpRequest.getDescription());
            help.setRequestContent(contentReq);
            help.setUser(user);
            help.setRequestType(sendHelpRequest.getHelpType());
            requestRepository.save(help);
        }else if(sendHelpRequest.getHelpType().equals("Create Company")){
            String contentReq = editFormat("Company name", sendHelpRequest.getCompanyName())
                                +editFormat("Company email", sendHelpRequest.getCompanyEmail())
                                +editFormat("Company description", sendHelpRequest.getCompanyDescription());
            help.setRequestContent(contentReq);
            help.setRequestType(sendHelpRequest.getHelpType());
            requestRepository.save(help);
        }
        return sendHelpRequest;
    }
    public static String extractCompanyEmail(String input) {
        String emailRegex = "Company email</strong><br/><p>([^<]+)</p>";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
    public static String extractCompanyName(String input) {
        String nameRegex = "Company name</strong><br/><p>([^<]+)</p>";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
    @Override
    public String updateRequestStatus(int requestId) {
        Request request = requestRepository.findById(requestId).get();
        Map<String, Object> templateModel = new HashMap<>(); 
        if(request.getRequestType().equals("Other")){
            templateModel.put("header", "Response Request");
            templateModel.put("message", "In the meantime, feel free to explore our <a href=\"http://www.example.com\">website</a> for more information about our services and features. We are constantly working to improve and add new functionalities to make your experience even better."); 
            String email = request.getUser().getEmail();
            emailService.sendEmailReplyReq(email, "Response Request", templateModel);
            request.setRequestStatus(true);
        }
        else if(request.getRequestType().equals("Create Company")){
            String email = extractCompanyEmail(request.getRequestContent());
            String company =extractCompanyName(request.getRequestContent());
            templateModel.put("header", "Response Request Create Company of "+company);
            templateModel.put("message1", "We are currently conducting a comprehensive review to ensure that your company meets the specific criteria and standards we have established. This review process is designed to assess your company's qualifications and suitability for our services. Rest assured, we will keep you informed of our progress and will contact you promptly once the review is complete. ");
            templateModel.put("message2", "Please keep an eye on your email as we will contact you and send you an account to access our system. Thank you!! :)))"); 
            emailService.sendEmailReplyReq(email, "Response Request Create Company of "+company, templateModel);
            request.setRequestStatus(true);
        }
        requestRepository.save(request);
        return "Updated request status successfully";
    }
}
