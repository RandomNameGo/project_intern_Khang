package swp.internmanagement.internmanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.SignupRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByRoleResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserResponse;
import swp.internmanagement.internmanagement.payload.response.GetInternResultFromCourseResponse;
import swp.internmanagement.internmanagement.payload.response.GetListAllInternResultResponse;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;
import swp.internmanagement.internmanagement.payload.response.InternAndDetailResponse;
import swp.internmanagement.internmanagement.payload.response.InternResultResponse;
import swp.internmanagement.internmanagement.payload.response.ListInternResultResponse;
import swp.internmanagement.internmanagement.payload.response.SearchUsersFunctionByMentorResponse;
import swp.internmanagement.internmanagement.payload.response.UserInSystemResponse;
import swp.internmanagement.internmanagement.payload.response.UserInfoResponse;
import swp.internmanagement.internmanagement.repository.InternTaskRepository;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;
import swp.internmanagement.internmanagement.security.jwt.JwtUtils;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserRepository userAccountRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InternTaskRepository internTaskRepository;
    @Autowired
    private InternTaskService internTaskService;

    public String generateUserName(String fullName, String role, int user_id) {
        String[] splitFullNames = fullName.split("\\s+");
        String lastName = splitFullNames[splitFullNames.length - 1];
        role = role.toLowerCase();
        String[] rolePart = role.split("_");
        StringBuilder codeRole = new StringBuilder();
        for (String part : rolePart) {
            if (part.length() > 0) {
                codeRole.append(part.charAt(0));
            }
        }
        String result = lastName.concat(codeRole.toString()).concat(String.valueOf(user_id));
        return result.toLowerCase();
    }

    // This method is for admin
    @Override
    public GetAllUserByParamResponse getAllUserAccountsByParam(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findUserAccountByParam(pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        GetAllUserByParamResponse getAllUserByParamResponse = new GetAllUserByParamResponse();
        getAllUserByParamResponse.setUserList(userAccountList);
        getAllUserByParamResponse.setPageNo(userAccounts.getNumber());
        getAllUserByParamResponse.setPageSize(userAccounts.getSize());
        getAllUserByParamResponse.setTotalItems(userAccounts.getTotalElements());
        getAllUserByParamResponse.setTotalPages(userAccounts.getTotalPages());

        return getAllUserByParamResponse;
    }

    // This method is for coordinator
    @Override
    public GetUserInSameCompanyResponse getUserInSameCompany(int companyId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findAllUsersInCompany(companyId, pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        GetUserInSameCompanyResponse getUserInSameCompanyResponse = new GetUserInSameCompanyResponse();
        getUserInSameCompanyResponse.setUserList(userAccountList);
        getUserInSameCompanyResponse.setPageNo(userAccounts.getNumber());
        getUserInSameCompanyResponse.setPageSize(userAccounts.getSize());
        getUserInSameCompanyResponse.setTotalItems(userAccounts.getTotalElements());
        getUserInSameCompanyResponse.setTotalPages(userAccounts.getTotalPages());

        return getUserInSameCompanyResponse;
    }

    @Override
    public GetAllUserByRoleResponse getAllUserByRole(int companyId, String role, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findAllMemberInCompany(companyId, role, pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        GetAllUserByRoleResponse getAllMentorResponse = new GetAllUserByRoleResponse();
        getAllMentorResponse.setUserAccountList(userAccountList);
        getAllMentorResponse.setPageNo(userAccounts.getNumber());
        getAllMentorResponse.setPageSize(userAccounts.getSize());
        getAllMentorResponse.setTotalItems(userAccounts.getTotalElements());
        getAllMentorResponse.setTotalPages(userAccounts.getTotalPages());

        return getAllMentorResponse;
    }

    @Transactional
    @Override
    public String deleteUserAccount(int userId) {
        if (!userAccountRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        UserAccount userAccount = userAccountRepository.findById(userId).get();
        userAccount.setStatus(null);
        userAccountRepository.save(userAccount);
        return "Deleted successfully";
        // // Optional<UserAccount> user = userAccountRepository.findById(userId);
        // // UserAccount newUser = user.get();
        // // newUser.setStatus(0);
        // // userRepository.save(newUser);
        // UserAccount userAccount = userAccountRepository.findById(userId).get();
        // if(userAccount.getRole().equals("ROLE_INTERN")) {
        // List<CourseIntern> courseInternList =
        // courseInternRepository.findByInternId(userId);
        // courseInternRepository.deleteAll(courseInternList);
        // List<InternTask> internTasks =
        // internTaskRepository.findInternTasksByInternId(userId);
        // internTaskRepository.deleteAll(internTasks);
        // List<CoordinatorFeedbackIntern> coordinatorFeedbackInterns =
        // coordinatorFeedbackInternRepository.findByInternId(userId);
        // coordinatorFeedbackInternRepository.deleteAll(coordinatorFeedbackInterns);
        // userAccountRepository.deleteById(userId);
        // List<MentorFeedbackIntern> mentorFeedbackInterns =
        // mentorFeedbackInternRepository.findByInternId(userId);
        // mentorFeedbackInternRepository.deleteAll(mentorFeedbackInterns);
        // }
        // if(userAccount.getRole().equals("ROLE_MENTOR")) {
        // List<MentorFeedbackIntern> mentorFeedbackInterns =
        // mentorFeedbackInternRepository.findByMentorId(userId);
        // mentorFeedbackInternRepository.deleteAll(mentorFeedbackInterns);
        // List<Course> courses = courseRepository.findByMentor(userId);
        // for(Course course : courses) {
        // course.setMentor(null);
        // courseRepository.save(course);
        // }
        // }
        // if(userAccount.getRole().equals("ROLE_COORDINATOR")) {
        // List<CoordinatorFeedbackIntern> coordinatorFeedbackInterns =
        // coordinatorFeedbackInternRepository.findByCoordinatorId(userId);
        // coordinatorFeedbackInternRepository.deleteAll(coordinatorFeedbackInterns);
        // }
    }

    @Override
    public boolean RegisterUser(List<SignupRequest> listSignUpRequest) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("verificationCode", "Click it to to change your password");
        try {
            LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
            for (SignupRequest signRequest : listSignUpRequest) {
                int id = userAccountRepository.findLastUserId() + 1;
                String userName = generateUserName(signRequest.getFullName(), signRequest.getRole(), id);
                UserAccount user = new UserAccount();
                InternDetail internDetail = new InternDetail();
                JobApplication jobApplication = new JobApplication();
                Company company = new Company();
                jobApplication.setId(signRequest.getJobApplicationId());
                company.setId(signRequest.getCompanyId());
                user.setUserName(userName);
                user.setPassword(encoder.encode("admin"));
                user.setFullName(signRequest.getFullName());
                user.setRole(signRequest.getRole());
                user.setEmail(signRequest.getEmail());
                UUID verifyCode = UUID.randomUUID();
                templateModel.put("verificationUrl", "http://localhost:3000/verify?code=" + verifyCode.toString()
                        + "&username=" + jwtUtils.generateTokenFromUsername(userName));
                templateModel.put("userName", "Your username: " + userName);
                user.setVerificationCode(verifyCode.toString());
                user.setDateOfBirth(dateOfBirth);
                user.setCompany(company);
                user.setStatus(0);
                internDetail.setUser(user);
                internDetail.setEducationBackground("Don't have");
                internDetail.setWorkHistory("don't have");
                user.setInternDetails(internDetail);
                emailService.sendEmail(signRequest.getEmail(), "Verify your email", templateModel);
                userAccountRepository.save(user);
                jobApplicationRepository.delete(jobApplication);
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean verifyAndActivate(String code, String userName, String password) {
        try {
            String userNameFromToken = jwtUtils.getUserNameFromJwtToken(userName);
            Optional<UserAccount> user = userAccountRepository.findByVerificationCodeAndUserName(code,
                    userNameFromToken);
            if (user.isPresent()) {
                user.get().setStatus(1);
                user.get().setPassword(encoder.encode(password));
                user.get().setVerificationCode(null);
                userAccountRepository.save(user.get());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<UserAccount> getAllUserAccountByRole(int companyId, String role) {
        return userRepository.findAllUserByRole(companyId, role);
    }

    @Override
    public boolean checkUserEsistAndSendEmail(String userName, String password) {
        try {
            Optional<UserAccount> user = userAccountRepository.findByUserName(userName);
            if (user.isPresent()) {
                UUID verificationcode = UUID.randomUUID();
                user.get().setVerificationCode(verificationcode.toString());
                userAccountRepository.save(user.get());
                sendEmailForgotPassword(userName, encoder.encode(password), verificationcode.toString());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void sendEmailForgotPassword(String username, String password, String verificationCode) {
        Map<String, Object> templateModel = new HashMap<>();
        String code = "code=" + verificationCode + "&userName=" + username + "&password=" + password;
        templateModel.put("verificationCode", "Click button below to confirm your changing");
        templateModel.put("verificationUrl",
                "http://localhost:3000/verifyPassword?code=" + jwtUtils.generateTokenFromUsername(code));
        try {
            emailService.sendEmailForgotPassword("anhtdse184413@fpt.edu.vn", "Verify your email", templateModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleChangePasswordUrl(String code) {
        try {
            String result = jwtUtils.getUserNameFromJwtToken(code);
            String codeVerifyPattern = "code=([^&]+)";
            String userNamePattern = "userName=([^&]+)";
            String passwordPattern = "password=([^&]+)";

            String codeVerify = extractValue(result, codeVerifyPattern);
            String userName = extractValue(result, userNamePattern);
            String password = extractValue(result, passwordPattern);

            Optional<UserAccount> user = userAccountRepository.findByVerificationCodeAndUserName(codeVerify,
                    userName);
            if (user.isPresent()) {
                user.get().setStatus(1);
                user.get().setPassword(encoder.encode(password));
                user.get().setVerificationCode(null);
                userAccountRepository.save(user.get());
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public GetAllUserResponse getAllUser(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findAllUserAccount(pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        List<UserInSystemResponse> userInfoResponseList = new ArrayList<>();
        for (UserAccount userAccount : userAccountList) {
            UserInSystemResponse userInfoResponse = new UserInSystemResponse();
            userInfoResponse.setId(userAccount.getId());
            userInfoResponse.setRole(userAccount.getRole());
            userInfoResponse.setEmail(userAccount.getEmail());
            userInfoResponse.setFullName(userAccount.getFullName());
            userInfoResponse.setCompanyName(userAccount.getCompany().getCompanyName());
            userInfoResponse.setCompany_id(userAccount.getCompany().getId());
            userInfoResponseList.add(userInfoResponse);
        }

        GetAllUserResponse getAllUserResponse = new GetAllUserResponse();
        getAllUserResponse.setUserList(userInfoResponseList);
        getAllUserResponse.setPageNo(userAccounts.getNumber());
        getAllUserResponse.setPageSize(userAccounts.getSize());
        getAllUserResponse.setTotalItems(userAccounts.getTotalElements());
        getAllUserResponse.setTotalPages(userAccounts.getTotalPages());
        return getAllUserResponse;
    }

    @Override
    public List<UserInfoResponse> getAllMentor(int companyId) {
        List<UserAccount> userAccounts = userAccountRepository.findAllMentorByCompanyId(companyId);
        List<UserInfoResponse> userInfoResponseList = new ArrayList<>();
        for (UserAccount userAccount : userAccounts) {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            userInfoResponse.setUser_id(userAccount.getId());
            userInfoResponse.setUsername(userAccount.getUserName());
            userInfoResponse.setEmail(userAccount.getEmail());
            userInfoResponse.setFullName(userAccount.getFullName());
            userInfoResponse.setRole(userAccount.getRole());
            userInfoResponse.setCompany_id(userAccount.getCompany().getId());
            userInfoResponseList.add(userInfoResponse);
        }
        return userInfoResponseList;
    }

    @Override
    public List<UserInfoResponse> getAllIntern(int companyId) {
        List<UserAccount> userAccounts = userAccountRepository.findAllInternByCompanyId(companyId);
        List<UserInfoResponse> userInfoResponseList = new ArrayList<>();
        for (UserAccount userAccount : userAccounts) {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            userInfoResponse.setUser_id(userAccount.getId());
            userInfoResponse.setUsername(userAccount.getUserName());
            userInfoResponse.setEmail(userAccount.getEmail());
            userInfoResponse.setFullName(userAccount.getFullName());
            userInfoResponse.setRole(userAccount.getRole());
            userInfoResponse.setCompany_id(userAccount.getCompany().getId());
            userInfoResponseList.add(userInfoResponse);
        }
        return userInfoResponseList;
    }

    @Override
    public GetListAllInternResultResponse getListAllInternResult(int companyId) {
        List<UserAccount> userAccounts = userAccountRepository.findAllInternByCompanyId(companyId);
        List<GetInternResultFromCourseResponse> getInternResultFromCourseResponses = new ArrayList<>();
        for (UserAccount userAccount : userAccounts) {
            GetInternResultFromCourseResponse getInternResultFromCourseResponse = new GetInternResultFromCourseResponse();

            long totalTask = internTaskRepository.countAllInternTasks(userAccount.getId());
            long totalCompletedTask = internTaskRepository.countAllInternTasksCompletedByIntern(userAccount.getId());
            double result = internTaskService.getTotalInternTaskResult(userAccount.getId());

            getInternResultFromCourseResponse.setInternId(userAccount.getId());
            getInternResultFromCourseResponse.setInternName(userAccount.getFullName());
            getInternResultFromCourseResponse.setTotalTask(totalTask);
            getInternResultFromCourseResponse.setCompletedTasks(totalCompletedTask);
            getInternResultFromCourseResponse.setResult(result);
            getInternResultFromCourseResponses.add(getInternResultFromCourseResponse);
        }
        GetListAllInternResultResponse getListAllInternResultResponse = new GetListAllInternResultResponse();
        getListAllInternResultResponse.setGetInternResultFromCourseResponses(getInternResultFromCourseResponses);
        return getListAllInternResultResponse;
    }

    @Override
    public void checkValidId(int companyId, int userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).get();
        if (userAccount.getCompany().getId() != companyId) {
            throw new RuntimeException("You are not allowed to access this company");
        }
    }
    
    public String editFormat(String title, String content){
        String tilteInDb = "<strong>"+title+"</strong><br/>";
        String contentInDb = "<p>"+content+"</p>";
        String result = tilteInDb+contentInDb;
        return result;
    }

    @Override
    public SearchUsersFunctionByMentorResponse searchByManager(int companyId, int userId, String role, int pageNo, int pageSize) {
        int pageNumber = 0;
        int Size = 0;
        long totalItems = 0;
        int totalPages = 0;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<InternAndDetailResponse> userInfoResponseList = new ArrayList<>();
        if(role == null){
            Page<UserAccount> userAccountPage = userAccountRepository.findAllByCompanyId(companyId, userId, pageable);
            List<UserAccount> userAccounts = userAccountPage.getContent();
            for (UserAccount userAccount : userAccounts) {
                InternAndDetailResponse userInfoResponse = new InternAndDetailResponse();
                userInfoResponse.setId(userAccount.getId());
                userInfoResponse.setFullName(userAccount.getUserName());
                userInfoResponse.setEmail(userAccount.getEmail());
                userInfoResponse.setFullName(userAccount.getFullName());
                userInfoResponse.setRole(userAccount.getRole());
                userInfoResponse.setCompany_id(userAccount.getCompany().getId());
                userInfoResponse.setCompanyName(userAccount.getCompany().getCompanyName());
                userInfoResponseList.add(userInfoResponse);
                pageNumber = userAccountPage.getNumber();
                Size = userAccountPage.getSize();
                totalItems = userAccountPage.getTotalElements();
                totalPages = userAccountPage.getTotalPages();
            }
        } else{
            Page<UserAccount> userAccountPage = userAccountRepository.findAllByRoleInCompany(companyId, userId, role, pageable);
            List<UserAccount> userAccounts = userAccountPage.getContent();
            for (UserAccount userAccount : userAccounts) {
                InternAndDetailResponse userInfoResponse = new InternAndDetailResponse();
                userInfoResponse.setId(userAccount.getId());
                userInfoResponse.setFullName(userAccount.getUserName());
                userInfoResponse.setEmail(userAccount.getEmail());
                userInfoResponse.setFullName(userAccount.getFullName());
                userInfoResponse.setRole(userAccount.getRole());
                userInfoResponse.setCompany_id(userAccount.getCompany().getId());
                userInfoResponse.setCompanyName(userAccount.getCompany().getCompanyName());
                String workHis=editFormat("Work History:", userAccount.getInternDetails().getWorkHistory());
                String edu=editFormat("Education Background:", userAccount.getInternDetails().getEducationBackground());
                userInfoResponse.setDetail(workHis+edu);
                userInfoResponseList.add(userInfoResponse);
                pageNumber = userAccountPage.getNumber();
                Size = userAccountPage.getSize();
                totalItems = userAccountPage.getTotalElements();
                totalPages = userAccountPage.getTotalPages();
            }
        }

        SearchUsersFunctionByMentorResponse searchUsersFunctionByMentor = new SearchUsersFunctionByMentorResponse();
        searchUsersFunctionByMentor.setUserInfoResponses(userInfoResponseList);
        searchUsersFunctionByMentor.setPageNo(pageNumber);
        searchUsersFunctionByMentor.setPageSize(Size);
        searchUsersFunctionByMentor.setTotalItems(totalItems);
        searchUsersFunctionByMentor.setTotalPages(totalPages);
        return searchUsersFunctionByMentor;
    }

    @Override
    public ListInternResultResponse getListInternResult(int companyId, int managerId) {

        UserAccount userAccount = userAccountRepository.findById(managerId).get();
        if (userAccount.getCompany().getId() != companyId) {
            throw new RuntimeException("You are not allowed to access this company");
        }

        List<UserAccount> interns = userAccountRepository.findAllInternByCompanyId(companyId);
        long internCompleteAll = 0;
        long internCompleteParticularly = 0;
        long internCompleteNothing = 0;
        long internDoesNotHaveTask = 0;
        List<UserAccount> listInternCompleteNothing = new ArrayList<>();
        List<UserAccount> listInternCompleteParticularly = new ArrayList<>();
        List<UserAccount> listInternCompleteAll = new ArrayList<>();
        List<UserAccount> listInternDoesNotHaveTask = new ArrayList<>();
        for (UserAccount intern : interns) {
            double internResult = internTaskService.getTotalInternTaskResult(intern.getId());
            if(internResult == 0){
                internCompleteNothing++;
                listInternCompleteNothing.add(intern);
            }
            if(internResult < 100 && internResult > 0){
                internCompleteParticularly++;
                listInternCompleteParticularly.add(intern);
            }
            if(internResult == 100){
                internCompleteAll++;
                listInternCompleteAll.add(intern);
            }
            if(Double.isNaN(internResult)) {
                internDoesNotHaveTask++;
                listInternDoesNotHaveTask.add(intern);
            }
        }

        long numOfIntern = userRepository.countInternByCompanyId(companyId);

        double internCompleteAllConvert = (double) internCompleteAll;
        double internCompleteParticularlyConvert =  (double) internCompleteParticularly;
        double internCompleteNothingConcert =  (double) internCompleteNothing;
        double internDoesNotHaveTaskConcert = (double) internDoesNotHaveTask;

        double statistical1 = (internCompleteAllConvert /numOfIntern) * 100;
        double statistical2 = (internCompleteParticularlyConvert /numOfIntern) * 100;
        double statistical3 = (internCompleteNothingConcert /numOfIntern) *100;
        double statistical4 = (internDoesNotHaveTaskConcert /numOfIntern) * 100;
        String reportParagraph = analysisReport(listInternCompleteNothing, listInternCompleteParticularly, listInternCompleteAll, listInternDoesNotHaveTask, statistical1, statistical2, statistical3, statistical4);
        InternResultResponse internResultResponse1 = new InternResultResponse();
        internResultResponse1.setValue(statistical1);
        internResultResponse1.setLabelName("Interns completed all tasks");

        InternResultResponse internResultResponse2 = new InternResultResponse();
        internResultResponse2.setValue(statistical2);
        internResultResponse2.setLabelName("Interns particularly completed tasks");

        InternResultResponse internResultResponse3 = new InternResultResponse();
        internResultResponse3.setValue(statistical3);
        internResultResponse3.setLabelName("Interns did not complete any tasks");

        InternResultResponse internResultResponse4 = new InternResultResponse();
        internResultResponse4.setValue(statistical4);
        internResultResponse4.setLabelName("Interns did not have tasks");

        List<InternResultResponse> internResultResponseList = new ArrayList<>();
        internResultResponseList.add(internResultResponse1);
        internResultResponseList.add(internResultResponse2);
        internResultResponseList.add(internResultResponse3);
        internResultResponseList.add(internResultResponse4);

        ListInternResultResponse listInternResultResponse = new ListInternResultResponse();
        listInternResultResponse.setInternResults(internResultResponseList);
        listInternResultResponse.setReportParagraph(reportParagraph);
        return listInternResultResponse;
    }

    public String analysisTask(
        double statistical,
        String paragraph100,
        String paragraph80,
        String paragraph70,
        String paragraph50,
        String paragraph40,
        String paragraph30,
        String paragraph20,
        String paragraph10,
        String paragraphBetween0and10,
        String paragraph0     
    ){
        String paragraph="";
        if (statistical == 100) {
            paragraph = paragraph100;
        } else if (statistical >= 80) {
            paragraph = paragraph80;
        } else if (statistical >= 70) {
            paragraph = paragraph70;
        } else if (statistical >= 50) {
            paragraph = paragraph50;
        } else if (statistical >= 40) {
            paragraph = paragraph40;
        } else if (statistical >= 30) {
            paragraph = paragraph30;
        } else if (statistical >= 20) {
            paragraph = paragraph20;
        } else if (statistical >= 10) {
            paragraph = paragraph10;
        } else if (statistical < 10 && statistical > 0) {
            paragraph = paragraphBetween0and10;
        } else if (statistical == 0) {
            paragraph = paragraph0;
        }
        return paragraph;
    }
    public String analysisComppleteTaskParticularly(
        double statisticalCompleteAll,
        double statisticalCompleteParticularly,
        double statisticalCompleteNothing,
        double statisticalDoesNotHaveTask    
    ){
        String paragraphCompleteParticularly="";
        if(statisticalCompleteNothing ==100){
            paragraphCompleteParticularly = "<p><span>All interns (100%) have completed part of their tasks</span>. This is quite alarming as it indicates that no intern has been able to fully complete all their tasks. Immediate adjustments and constructive feedback are necessary to improve progress and achieve better results.</p>";
        }
        else if (statisticalCompleteParticularly >= 80) {
            paragraphCompleteParticularly = "<p>This is the largest section of the chart, making up <span>" + statisticalCompleteParticularly + "%</span> of it. This indicates that a majority of interns have partially completed their tasks. However, the high percentage suggests that there might be systemic issues or difficulties faced by the interns that need to be addressed to help them achieve full task completion.</p>";
        } else if (statisticalCompleteParticularly >= 60) {
            paragraphCompleteParticularly = "<p>This section, comprising <span>" + statisticalCompleteParticularly + "%</span> of the chart, shows that a significant portion of interns have partially completed their tasks. This performance, while showing effort, indicates a need for better support and possibly more realistic task assignments to ensure interns can complete all their tasks.</p>";
        } else if (statisticalCompleteParticularly >= 40) {
            paragraphCompleteParticularly = "<p>Making up <span>" + statisticalCompleteParticularly + "%</span> of the chart, this indicates that while some interns have made progress in their tasks, a large portion did not complete them fully. This suggests that additional resources or adjustments in task assignments may be necessary to help interns complete their work more effectively.</p>";
        } else if (statisticalCompleteParticularly >= 30) {
            paragraphCompleteParticularly = "<p>This section represents <span>" + statisticalCompleteParticularly + "%</span> of the chart. This indicates that nearly a third of the interns were able to partially complete their tasks, but there is significant room for improvement. Providing targeted training and support could help improve these outcomes.</p>";
        } else if (statisticalCompleteParticularly >= 20) {
            paragraphCompleteParticularly = "<p>Accounting for <span>" + statisticalCompleteParticularly + "%</span> of the chart, this suggests that a notable portion of interns struggled with completing their assignments. This highlights the need for more robust support systems and perhaps a reevaluation of the task difficulty or clarity. Addressing these issues can help improve the overall performance of the interns.</p>";
        } else if (statisticalCompleteParticularly >= 10) {
            paragraphCompleteParticularly = "<p>This section, which makes up <span>" + statisticalCompleteParticularly + "%</span> of the chart, indicates a considerable need for additional support and resources to help interns complete their work. Such a low percentage calls for an in-depth review of the intern training program and the obstacles they face, so that effective solutions can be implemented to enhance their performance.</p>";
        } else if (statisticalCompleteParticularly < 10 && statisticalCompleteParticularly > 0) {
            paragraphCompleteParticularly = "<p>This small section of the chart, accounting for less than 10% of interns <span>(" + statisticalCompleteParticularly + "%)</span>, shows that only a few interns partially completed their tasks. This is a critical issue that requires immediate attention to understand and resolve the underlying problems. Such a low completion rate is concerning and indicates that significant changes are needed in our approach to intern training and task assignment.</p>";
        } else if (statisticalCompleteParticularly == 0) {
            if (statisticalCompleteAll == 100) {
                paragraphCompleteParticularly = "<p>This is a good sign because all interns have completed the tasks assigned to them without skipping any. The <span>0%</span> rate of interns completing only part of the tasks is something very commendable and should be maintained.</p>";
            } else if (statisticalCompleteNothing == 100) {
                paragraphCompleteParticularly = "<p><span>The rate of interns particularly completing tasks is 0%</span> because all interns did not complete any tasks. This needs improvement and adjustments to be made.</p>";
            } else if (statisticalDoesNotHaveTask == 100) {
                paragraphCompleteParticularly = "<p><span>The rate of interns particularly completing tasks is 0%</span> because all interns were not assigned any tasks or none of them participated in any courses. This situation rarely occurs and requires appropriate adjustments to limit this.</p>";
            }
            else if (statisticalCompleteAll > 0 && statisticalCompleteNothing > 0 && statisticalDoesNotHaveTask > 0) {
                paragraphCompleteParticularly = "<p><span>The rate of interns partially completing tasks is 0%</span> because the rates of interns who completed all tasks, did not complete any tasks, and did not have any tasks account for the entire percentage. This indicates that the training courses have been effective as some interns have completed all the tasks assigned. However, the data also shows that there are still interns who did not do any tasks and those who were not assigned any tasks. You need to find solutions to address this to improve performance.</p>";
            } else if (statisticalCompleteAll > 0 && statisticalCompleteNothing > 0) {
                paragraphCompleteParticularly = "<p><span>The rate of interns partially completing tasks is 0%</span> because the rates of interns who completed all tasks and those who did not complete any tasks are quite high. This also poses challenges as there are still interns who did not do any tasks.</p>";
            } else if (statisticalCompleteAll > 0 && statisticalDoesNotHaveTask > 0) {
                paragraphCompleteParticularly = "<p><span>The rate of interns partially completing tasks is 0%</span> because the rates of interns who completed all tasks and those who did not have any tasks are quite high. This also poses challenges as there are still interns who were not assigned any tasks. Solutions are needed to address this.</p>";
            } else if (statisticalDoesNotHaveTask > 0 && statisticalCompleteNothing > 0) {
                paragraphCompleteParticularly = "<p><span>The rate of interns partially completing tasks is 0%</span> because the rates of interns who did not do any tasks and those who did not have any tasks account for almost the entire percentage. This is a limitation in task organization, and solutions are needed to address this to improve training performance.</p>";
            }            
        }
        return paragraphCompleteParticularly;
    }
    public String analysisEachIntern(List<UserAccount> list){
        String paragraphRes="";
        String title="<h5><strong>Detail Of Each Intern</strong></h5>";
        if (list.isEmpty()) {
            return title+"<p>No intern found hear!!!</p>";
        }
        for (UserAccount intern : list) {
            long totalTask = internTaskRepository.countAllInternTasks(intern.getId());
            long totalCompletedTask = internTaskRepository.countAllInternTasksCompletedByIntern(intern.getId());
            long taskNotComplate = totalTask-totalCompletedTask;
            String paragraph = "<p>"+intern.getFullName()+": There are <span>"+totalCompletedTask+" tasks completed</span> out of a total of <span>"+totalTask+" task</span>, and <span>"+taskNotComplate+" not yet completed</span>"+"</p>";
            paragraphRes+=paragraph;
        }
        return title+paragraphRes;
    }
    public String analysisReport(
        List<UserAccount> listInternCompleteNothing,
        List<UserAccount> listInternCompleteParticularly,
        List<UserAccount> listInternCompleteAll,
        List<UserAccount> listInternDoesNotHaveTask,
        double statisticalCompleteAll,
        double statisticalCompleteParticularly,
        double statisticalCompleteNothing,
        double statisticalDoesNotHaveTask
    ){
        int totalIntern = listInternCompleteAll.size() +listInternCompleteNothing.size()+listInternCompleteParticularly.size()+listInternDoesNotHaveTask.size();
        String header ="<h1>"+"Detailed Report on InternShip Task Completion Status"+"</h1>";
        String bodyTilteOverview = "<h2>"+"<strong>OverView</strong>"+"</h2>";
        String bodyTilteDetailAnalysis = "<h2>"+"<strong>Detailed Analysis</strong>"+"</h2>";
        String bodyTilteConclusion = "<h2>"+"<strong>Conclusion</strong>"+"</h2>";
        String sectionTilteCompleteAll = "<h3><strong>"+"Interns completed all tasks ("+statisticalCompleteAll+"%):</strong></h3>";
        String sectionTilteCompleteParticularly = "<h3><strong>"+"Interns particularly completed tasks ("+statisticalCompleteParticularly+"%):</strong></h3>";
        String sectionTilteCompleteNothing = "<h3><strong>"+"Interns did not complete any tasks ("+statisticalCompleteNothing+"%):</strong></h3>";
        String sectionTilteDoesNotHaveTask = "<h3><strong>"+"Interns did not have tasks ("+statisticalDoesNotHaveTask+"%):</strong></h3>";
        String summary = "<p>Overall, the analysis of intern task completion reveals several critical insights into the performance and engagement of interns within the company.</p>";
        String strengths = "<p><strong>Strengths:</strong> It is commendable that a significant percentage of interns were able to complete all tasks assigned to them. This reflects the efficiency and dedication of both the interns and the support systems in place.</p>";
        String areasForImprovement = "<p><strong>Areas for Improvement:</strong> There are notable gaps in task completion, particularly among those who did not complete any tasks or were not assigned any tasks. This suggests the need for better task assignment processes and more robust support for interns facing difficulties.</p>";
        String recommendations = "<p><strong>Recommendations:</strong> To address these issues, the following actions are recommended:</p>"
                                + "<ul>"
                                + "<li>Implement a more structured task assignment process to ensure all interns are given meaningful work.</li>"
                                + "<li>Provide additional support and resources for interns who are struggling to complete their tasks.</li>"
                                + "<li>Conduct regular check-ins and feedback sessions to identify and resolve any issues early.</li>"
                                + "<li>Enhance training programs to better prepare interns for their tasks.</li>"
                                + "</ul>";
        String closingStatement = "<p>By taking these steps, we can improve the overall performance and satisfaction of our interns, ensuring a more effective and fulfilling internship experience. Continuous evaluation and improvement of the internship program will help maintain high standards and achieve better results in the future.</p>";

        String bodyContentOverView = "<p>This report provides a detailed analysis of the task completion status of interns. The chart is divided into four main categories: </p>"
        +"<p>There are <strong><span>"+totalIntern+" interns</span></strong> in your company</p>"
        + "<ul>"
        + "<li><strong>Interns who completed all tasks:</strong> This category represents <strong>" + statisticalCompleteAll + "% of the interns</strong>, indicating those who were able to successfully complete all the tasks assigned to them.</li>"
        + "<li><strong>Interns who partially completed tasks:</strong> This category accounts for <strong>" + statisticalCompleteParticularly + "% of the interns</strong>. These interns were able to complete some but not all of their assigned tasks, suggesting areas where additional support or time might be necessary.</li>"
        + "<li><strong>Interns who did not complete any tasks:</strong> Making up <strong>" + statisticalCompleteNothing + "% of the interns</strong>, this category highlights those who were unable to complete any tasks. This raises concerns about possible issues in training, task assignment, or intern engagement.</li>"
        + "<li><strong>Interns without tasks:</strong> This final category includes <strong>" + statisticalDoesNotHaveTask + "% of the interns</strong> who did not have any tasks assigned to them. This suggests a need for better coordination in task assignment and intern placement to ensure all interns have the opportunity to contribute and gain experience.</li>"
        + "</ul>"
        + "<p>By analyzing these categories, we can identify strengths and areas for improvement in our internship program, ensuring that all interns receive the support and opportunities they need to succeed.</p>";
        String paragraphCompleteAll = analysisTask(
            statisticalCompleteAll,
            "<p><span>All interns (100%) have completed all the tasks</span> assigned to them. This shows an exceptional level of commitment, efficiency, and determination from our interns. Their ability to complete all tasks assigned to them reflects not only their individual capabilities but also the effectiveness of the training and support systems in place. This is a remarkable achievement that sets a high standard for future interns.</p>",
            "<p>This is the largest section of the chart, making up <span>" + statisticalCompleteAll + "%</span> of it. This indicates that a majority of interns have completed all tasks, highlighting a high level of productivity and dedication among the team. Their performance is commendable and shows that they are on the right path to achieving full task completion. However, there is still a small margin for improvement to reach the ideal goal of 100% completion.</p>",
            "<p>This section, comprising <span>" + statisticalCompleteAll + "%</span> of the chart, shows that a significant portion of interns completed all tasks. This is a strong performance that demonstrates their hard work and capability, though there's room for improvement. By providing additional resources and support, we can help more interns reach this level of success and potentially increase this percentage in future evaluations.</p>",
            "<p>Making up <span>" + statisticalCompleteAll + "%</span> of the chart, this indicates that while a good number of interns have successfully completed their tasks, there is still a notable portion that did not. This is a good outcome, but there are opportunities to support the remaining interns more effectively to help them achieve full task completion. Enhanced mentorship programs and clearer task guidelines might contribute to improving these results.</p>",
            "<p>This section represents <span>" + statisticalCompleteAll + "%</span> of the chart. This indicates that nearly half of the interns were able to meet all requirements, but there is significant room for improvement. To address this, it may be beneficial to conduct a detailed analysis to understand the challenges faced by interns and implement strategies to overcome these obstacles.</p>",
            "<p>Accounting for <span>" + statisticalCompleteAll + "%</span> of the chart, this suggests that a notable portion of interns struggled with completing all assignments. This highlights the need for more robust support systems and perhaps a reevaluation of the task difficulty or clarity. Addressing these issues can help improve the overall performance of the interns.</p>",
            "<p>This section, which makes up <span>" + statisticalCompleteAll + "%</span> of the chart, indicates a considerable need for additional support and resources to help interns complete their work. Such a low percentage calls for an in-depth review of the intern training program and the obstacles they face, so that effective solutions can be implemented to enhance their performance.</p>",
            "<p>Representing <span>" + statisticalCompleteAll + "%</span> of the chart, this low percentage indicates substantial challenges that need to be addressed. It is crucial to investigate the reasons behind this low completion rate and to provide targeted interventions to support the interns in overcoming these challenges. This will not only improve their performance but also boost their morale and confidence.</p>",
            "<p>This small section of the chart, accounting for less than 10% of interns <span>(" + statisticalCompleteAll + "%)</span>, shows that only a few interns completed all tasks. This is a critical issue that requires immediate attention to understand and resolve the underlying problems. Such a low completion rate is concerning and indicates that significant changes are needed in our approach to intern training and task assignment.</p>",
            "<p>No interns <span>(0%) completed all tasks</span>. This alarming situation calls for a thorough review of the tasks, support systems, and possible obstacles faced by the interns. Immediate corrective actions are necessary to address this issue and to ensure that future interns are better supported and equipped to complete their assignments successfully.</p>"
        );
        String paragraphCompleteNothing = analysisTask(
            statisticalCompleteNothing,
"<p><span>All interns (100%) failed to complete any tasks</span>. This is an extremely concerning situation that indicates a fundamental issue with either the training program, the task assignment process, or the interns' engagement. Immediate and thorough investigation is required to identify and address the root causes of this failure.</p>",
            "<p>This is the largest section of the chart, making up <span>" + statisticalCompleteNothing + "%</span> of it. This very high percentage of interns failing to complete any tasks is alarming and suggests significant issues within the training program or task assignment process. Immediate actions are necessary to identify and resolve the obstacles preventing these interns from completing their work.</p>",
            "<p>Making up <span>" + statisticalCompleteNothing + "%</span> of the chart, this substantial portion of interns did not complete any tasks. This indicates a serious lack of engagement or understanding of the tasks assigned. It's crucial to investigate the reasons behind this and provide necessary support and guidance to help interns improve their performance.</p>",
            "<p>This is one of the largest sections of the chart, making up <span>" + statisticalCompleteNothing + "%</span> of it. This high percentage of interns failing to complete any tasks is concerning and indicates a serious issue within the training program or task assignment process. Immediate actions are necessary to identify and resolve the obstacles preventing these interns from completing their work.</p>",
            "<p>Making up <span>" + statisticalCompleteNothing + "%</span> of the chart, this significant portion of interns did not complete any tasks. This indicates a lack of engagement or understanding of the tasks assigned. It's crucial to investigate the reasons behind this and provide necessary support and guidance to help interns improve their performance.</p>",
            "<p>This section, comprising <span>" + statisticalCompleteNothing + "%</span> of the chart, shows that a considerable number of interns failed to complete any tasks. This suggests a need for a thorough review of the task assignment process and the support provided to interns. Addressing these issues can help increase task completion rates.</p>",
            "<p>Representing <span>" + statisticalCompleteNothing + "%</span> of the chart, this section indicates that a notable portion of interns did not complete any tasks. This calls for immediate intervention to understand the challenges faced by these interns and to implement effective solutions to enhance their task completion rates.</p>",
            "<p>This section makes up <span>" + statisticalCompleteNothing + "%</span> of the chart, showing that some interns did not complete any tasks. While this percentage is lower, it still requires attention to understand and address the reasons behind their lack of completion.</p>",
            "<p>This small section of the chart, accounting for less than 10% of interns <span>(" + statisticalCompleteNothing + "%)</span>, shows that a few interns did not complete any tasks. While this is a smaller percentage, it still highlights the need for better support and possibly clearer task instructions to ensure all interns can complete their assignments.</p>",
            "<p><span>No interns (0%) failed to complete any tasks</span>. This is a positive outcome, indicating that all interns made some progress on their assignments.</p>"
        );
        String paragraphDoesNotHaveTask = analysisTask(
        statisticalDoesNotHaveTask,
    "<p><span>All interns (100%) did not have any tasks assigned</span>. This indicates a severe issue with the mentorship or coordination process. It is critical to investigate whether mentors are not assigning tasks or if the intern coordinator is not placing interns in appropriate courses. Immediate and comprehensive action is required to rectify this problem.</p>",
        "<p>This is the largest section of the chart, making up <span>" + statisticalDoesNotHaveTask + "%</span> of it. This very high percentage of interns without tasks suggests significant deficiencies in task assignment by mentors or placement by the intern coordinator. Urgent measures are needed to ensure that all interns are provided with meaningful work assignments.</p>",
        "<p>Making up <span>" + statisticalDoesNotHaveTask + "%</span> of the chart, this substantial portion of interns did not have any tasks. This highlights a serious gap in either the task assignment by mentors or the intern placement process. It is essential to review and improve these processes to ensure better engagement of interns.</p>",
        "<p>This is one of the largest sections of the chart, making up <span>" + statisticalDoesNotHaveTask + "%</span> of it. This high percentage of interns without tasks is concerning and indicates a major issue with task assignment or intern placement. Immediate actions are necessary to identify and address the reasons why so many interns are left without tasks.</p>",
        "<p>Making up <span>" + statisticalDoesNotHaveTask + "%</span> of the chart, this significant portion of interns did not have any tasks assigned. This suggests a lack of proper task assignment by mentors or ineffective placement by the intern coordinator. It is crucial to investigate and provide the necessary support to ensure all interns receive tasks.</p>",
        "<p>This section, comprising <span>" + statisticalDoesNotHaveTask + "%</span> of the chart, shows that a considerable number of interns did not have tasks. This indicates the need for a thorough review of the task assignment and intern placement processes. Addressing these issues can help ensure that more interns are provided with meaningful tasks.</p>",
        "<p>Representing <span>" + statisticalDoesNotHaveTask + "%</span> of the chart, this section indicates that a notable portion of interns did not have any tasks. This calls for immediate intervention to understand the challenges in task assignment and placement and to implement effective solutions to ensure interns are engaged with tasks.</p>",
        "<p>This section makes up <span>" + statisticalDoesNotHaveTask + "%</span> of the chart, showing that some interns did not have any tasks. While this percentage is lower, it still requires attention to understand and address the reasons behind their lack of task assignments.</p>",
        "<p>This small section of the chart, accounting for less than 10% of interns <span>(" + statisticalDoesNotHaveTask + "%)</span>, shows that a few interns did not have tasks. While this is a smaller percentage, it still highlights the need for better support and clearer processes to ensure all interns receive tasks.</p>",
    "<p><span>No interns (0%) were left without tasks</span>. This is a positive outcome, indicating that all interns were assigned tasks and had the opportunity to engage in meaningful work.</p>"
        );
        String paragraphCompleteParticularly=analysisComppleteTaskParticularly(statisticalCompleteAll, statisticalCompleteParticularly, statisticalCompleteNothing, statisticalDoesNotHaveTask);
        String internCompletedAllTask = analysisEachIntern(listInternCompleteAll);
        String internCompleteParticularly = analysisEachIntern(listInternCompleteParticularly);
        String internCompleteNothing = analysisEachIntern(listInternCompleteNothing);
        String internDoesNotHaveTask = analysisEachIntern(listInternDoesNotHaveTask);
        String bodyOverView=bodyTilteOverview+bodyContentOverView;
        String completeAllBody =sectionTilteCompleteAll+paragraphCompleteAll+internCompletedAllTask;
        String completeParticularly=sectionTilteCompleteParticularly+paragraphCompleteParticularly+internCompleteParticularly;
        String completeNothing = sectionTilteCompleteNothing+paragraphCompleteNothing+internCompleteNothing;
        String doesNotHaveTask=sectionTilteDoesNotHaveTask+paragraphDoesNotHaveTask+internDoesNotHaveTask;
        String bodyDetailAnalysis=bodyTilteDetailAnalysis+completeAllBody+completeParticularly+completeNothing+doesNotHaveTask;
        String conclusion=bodyTilteConclusion+summary+strengths+areasForImprovement+recommendations+closingStatement;
        String response ="<div>"+header+bodyOverView+bodyDetailAnalysis+conclusion+"</div>";
        return response;
    }

    private static String extractValue(String input, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
