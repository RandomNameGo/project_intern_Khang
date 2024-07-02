package swp.internmanagement.internmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.Field;
import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.entity.Request;
// import swp.internmanagement.internmanagement.payload.request.HelpRequest;
// import swp.internmanagement.internmanagement.service.JobService;
// import swp.internmanagement.internmanagement.service.RequestService;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.payload.request.LoginRequest;
import swp.internmanagement.internmanagement.payload.response.CompanyNameResponse;
import swp.internmanagement.internmanagement.payload.request.SendHelpRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllFieldsResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllJobRes;
import swp.internmanagement.internmanagement.payload.response.GetAllJobsResponse;
import swp.internmanagement.internmanagement.payload.response.SearchJobsResponse;
import swp.internmanagement.internmanagement.service.*;

@RestController
@RequestMapping("/internbridge")
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

    @Autowired
    private JobService jobService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/jobs")
    public ResponseEntity<GetAllJobRes> getAllJobs(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(jobService.getAllJobs(pageNo, pageSize));
    }

    // @GetMapping("/jobs/id={id}")
    // public ResponseEntity<SearchJobsResponse> getJobID(
    // @PathVariable Integer id,
    // @RequestParam(value = "pageNo", defaultValue = "0", required = false) int
    // pageNo,
    // @RequestParam(value = "pageSize", defaultValue = "0", required = false) int
    // pageSize) {
    // return ResponseEntity.ok(jobService.getJobById(id, pageNo, pageSize));
    // }
    @GetMapping("/jobs/id={id}")
    public ResponseEntity<Job> getJobId(@PathVariable Integer id) {
        Job job = jobService.getJobId(id);
        return job != null ? ResponseEntity.ok(job) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/jobs/{jobName}")
    public ResponseEntity<SearchJobsResponse> getJob(
            @PathVariable String jobName,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(jobService.getJobs(jobName, pageNo, pageSize));
    }

    @GetMapping("/fields")
    public ResponseEntity<GetAllFieldsResponse> getAllFields(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(fieldService.getAllFields(pageNo, pageSize));
    }

    @GetMapping("/field")
    public List<Field> getMethodName() {
        return fieldService.getField();
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<?> sendRequest(@RequestBody SendHelpRequest sendHelpRequest) {
        return new ResponseEntity<>(requestService.saveHelpRequest(sendHelpRequest), HttpStatus.CREATED);
    }

    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping("/applyjob")
    public ResponseEntity<?> ApplyJob(
            @RequestParam("jobId") Integer jobId,
            @RequestParam("email") String email,
            @RequestParam("fullName") String fullName,
            @RequestParam("cv") MultipartFile cv) {
        JobApplicationRequest jobApplicationRequest = new JobApplicationRequest();
        jobApplicationRequest.setJobId(jobId);
        jobApplicationRequest.setEmail(email);
        jobApplicationRequest.setFullName(fullName);
        jobApplicationRequest.setCV(cv);
        boolean result = jobApplicationService.addJobApplication(jobApplicationRequest);
        if (result) {
            return ResponseEntity.ok("Job application submitted successfully.");
        } else {
            return ResponseEntity.status(500).body("Failed to submit job application.");
        }
    }

    @PutMapping("/verify")
    public ResponseEntity<?> VerifyAndActivate(
            @RequestParam("code") String code,
            @RequestParam("userName") String userName,
            @RequestParam("password") String password) {
        try {
            boolean check = userAccountService.verifyAndActivate(code, userName, password);
            if (check) {
                return ResponseEntity.ok("Success to activate");
            } else {
                return ResponseEntity.status(500).body("Error to activate");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error to activate");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getPassword() != null && loginRequest.getUsername() != null) {
                boolean check = userAccountService.checkUserEsistAndSendEmail(loginRequest.getUsername(),
                        loginRequest.getPassword());
                if (check) {
                    userAccountService.checkUserEsistAndSendEmail(loginRequest.getUsername(),
                            loginRequest.getPassword());
                    return ResponseEntity.ok("Success to change please check your mail");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error to activate");
        }
        return ResponseEntity.status(500).body("Error to activate");
    }

    @PutMapping("/verifyPassword")
    public ResponseEntity<?> VerificationPasswordChanging(@RequestParam("code") String code) {
        try {
            if (code != null) {
                boolean check = userAccountService.handleChangePasswordUrl(code);
                if (check) {
                    return ResponseEntity.ok("Success to sending");
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error to activate");
        }
        return ResponseEntity.status(500).body("Error to activate");
    }

    @GetMapping("/companyName/{companyId}")
    public ResponseEntity<CompanyNameResponse> getCompany(@PathVariable Integer companyId) {
        return ResponseEntity.ok(companyService.getCompanyName(companyId));
    }
    @PutMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmailJob(@RequestParam("code") String code) {
        try {
            if (code != null) {
                boolean check = jobApplicationService.handleVerifyEmailJob(code);
                if (check) {
                    return ResponseEntity.ok("Success to sending");
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error to activate");
        }
        return ResponseEntity.status(500).body("Error to activate");
    }
}