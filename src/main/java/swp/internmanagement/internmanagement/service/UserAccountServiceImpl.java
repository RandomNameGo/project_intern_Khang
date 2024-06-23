package swp.internmanagement.internmanagement.service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.SignupRequest;
import swp.internmanagement.internmanagement.payload.response.*;
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

    @Override
    public String deleteUserAccount(int userId) {
        if (!userAccountRepository.existsById(userId)) {
            return "User not found";
        }
        userAccountRepository.deleteById(userId);
        return "Deleted successfully";
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
                InternDetail internDetail=new InternDetail();
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
        }return false;
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

    private static String extractValue(String input, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }



}
