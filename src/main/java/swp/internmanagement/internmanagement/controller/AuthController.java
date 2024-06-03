package swp.internmanagement.internmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.LoginRequest;
import swp.internmanagement.internmanagement.payload.request.SignupRequest;
import swp.internmanagement.internmanagement.payload.response.MessageResponse;
import swp.internmanagement.internmanagement.payload.response.UserInfoResponse;
import swp.internmanagement.internmanagement.repository.UserRepository;
import swp.internmanagement.internmanagement.security.jwt.JwtUtils;
import swp.internmanagement.internmanagement.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/internbridge/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;


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
    

    @PostMapping("/signin")
    public ResponseEntity <?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication=authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
            ResponseCookie jwtCookie=jwtUtils.generateJwtCookie(userDetails);
            String role= userDetails.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new UserInfoResponse(userDetails.getUser_id(),userDetails.getUsername(),userDetails.getEmail(),role,userDetails.getCompany_id()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("An error occured while processing your request"));
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody List<SignupRequest> listSignUpRequest) {
        try {
            LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
            for (SignupRequest signRequest : listSignUpRequest) {
                int id=userRepository.findLastUserId()+1;
                String userName=generateUserName(signRequest.getFullName(), signRequest.getRole(), id);
                UserAccount user= new UserAccount();
                Company company= new Company();
                company.setId(signRequest.getCompany_id());
                user.setUserName(userName);
                user.setPassword(encoder.encode("admin"));
                user.setFullName(signRequest.getFullName());
                user.setRole(signRequest.getRole());
                user.setEmail(signRequest.getEmail());
                user.setDateOfBirth(dateOfBirth);
                user.setCompany(company);
                userRepository.save(user);
            }

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception ex) {
            ex.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while processing your request."));
        }
    }
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
