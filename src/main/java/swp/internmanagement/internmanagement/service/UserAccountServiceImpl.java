package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByRoleResponse;
import swp.internmanagement.internmanagement.payload.request.SignupRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;
import swp.internmanagement.internmanagement.payload.response.MessageResponse;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;
import swp.internmanagement.internmanagement.security.jwt.JwtUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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


    //This method is for admin
    @Override
    public GetAllUserByParamResponse getAllUserAccountsByParam(String param, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserAccount> userAccounts = userAccountRepository.findUserAccountByParam(param, pageable);
        List<UserAccount> userAccountList = userAccounts.getContent();

        GetAllUserByParamResponse getAllUserByParamResponse = new GetAllUserByParamResponse();
        getAllUserByParamResponse.setUserList(userAccountList);
        getAllUserByParamResponse.setPageNo(userAccounts.getNumber());
        getAllUserByParamResponse.setPageSize(userAccounts.getSize());
        getAllUserByParamResponse.setTotalItems(userAccounts.getTotalElements());
        getAllUserByParamResponse.setTotalPages(userAccounts.getTotalPages());

        return getAllUserByParamResponse;
    }

    //This method is for coordinator
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
        if(!userAccountRepository.existsById(userId)) {
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
                int id=userAccountRepository.findLastUserId()+1;
                String userName=generateUserName(signRequest.getFullName(), signRequest.getRole(), id);
                UserAccount user= new UserAccount();
                JobApplication jobApplication=new JobApplication();
                Company company= new Company();
                jobApplication.setId(signRequest.getJobApplicationId());
                company.setId(signRequest.getCompanyId());
                user.setUserName(userName);
                user.setPassword(encoder.encode("admin"));
                user.setFullName(signRequest.getFullName());
                user.setRole(signRequest.getRole());
                user.setEmail(signRequest.getEmail());
                UUID verifyCode =UUID.randomUUID();
                templateModel.put("verificationUrl", "http://localhost:3000/verify?code=" +verifyCode.toString()+"&username="+jwtUtils.generateTokenFromUsername(userName));
                user.setVerificationCode(verifyCode.toString());
                user.setDateOfBirth(dateOfBirth);
                user.setCompany(company);
                user.setStatus(0);
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
    public boolean verifyAndActivate(String code, String userName) {
        try {
            String userNameFromToken =jwtUtils.getUserNameFromJwtToken(userName);
            Optional<UserAccount> user = userAccountRepository.findByVerificationCodeAndUserName(code, userNameFromToken);
            if(user.isPresent()){
                user.get().setStatus(1);
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
}
