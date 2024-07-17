package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateCompanyRequest;
import swp.internmanagement.internmanagement.payload.response.AcceptedJobApplicationResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;
import swp.internmanagement.internmanagement.service.CompanyService;
import swp.internmanagement.internmanagement.service.JobApplicationService;
import swp.internmanagement.internmanagement.service.JobService;
import swp.internmanagement.internmanagement.service.RequestService;
import swp.internmanagement.internmanagement.service.UserAccountService;


@RestController
@RequestMapping("/internbridge/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    CompanyService companyService;

    @Autowired
    private JobService jobService;

    @GetMapping(value = "/request", produces = "application/json")
    public ResponseEntity<GetAllRequestResponse> getAllRequestResponse(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(requestService.getRequests(pageNo, pageSize));
    }

//    @GetMapping("/search/param={param}")
//    public ResponseEntity<GetAllUserByParamResponse> getAllUserByParamResponse(
//            @PathVariable String param,
//            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
//            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
//        return ResponseEntity.ok(userAccountService.getAllUserAccountsByParam(param, pageNo, pageSize));
//    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllUserResponse(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(userAccountService.getAllUser(pageNo, pageSize));
    }
    
    @GetMapping("/jobApplication")
    public ResponseEntity<AcceptedJobApplicationResponse> getAllAcceptedJobApplication(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(jobApplicationService.getAllAcceptedJobApplication(pageNo, pageSize));
    }

    @PostMapping("/createCompany")
    public ResponseEntity<?> createCompany(
        @RequestParam("companyName") String companyName,
        @RequestParam("companyDescription") String companyDescription,
        @RequestParam("location") String location,
        @RequestParam("image") MultipartFile image) {
    try {
        CreateCompanyRequest companyRequest = new CreateCompanyRequest();
        companyRequest.setCompanyName(companyName);
        companyRequest.setCompanyDescription(companyDescription);
        companyRequest.setLocation(location);
        companyRequest.setImage(image);
        
        boolean check = companyService.checkExistedCompanyAndInsert(companyRequest);
        if (check) {
            return ResponseEntity.ok("Create job submitted successfully.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Failed to create company.");
    }
    return ResponseEntity.status(500).body("Failed to create company.");
}
    @GetMapping("/viewCompany")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCompanyInCurrent(
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize
    ) {
        try {
            return ResponseEntity.ok(companyService.getAllCompanyResponse(pageNo, pageSize));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to get company.");
        }
    }
    

    @PutMapping("/userAccount/delete/id={userId}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(userAccountService.deleteUserAccount(userId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/company/delete/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable int companyId) {
        return ResponseEntity.ok(companyService.deleteCompany(companyId));
    }

    @PutMapping("/company/update/{companyId}")
    public ResponseEntity<?> updateCompany(@PathVariable int companyId ,@RequestBody UpdateCompanyRequest companyRequest) {
        return ResponseEntity.ok(companyService.updateCompany(companyId, companyRequest));
    }

    @DeleteMapping("/job/delete/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable int jobId) {
        try {
            boolean delete = jobService.deleteJob(jobId);
            if (delete) {
                return ResponseEntity.ok("Delete job submitted successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete job.");
        }
        return ResponseEntity.status(500).body("Job is not found.");
    }
    @GetMapping("/AllCompany")
    public ResponseEntity<?> getAllCompany() {
        return ResponseEntity.ok(companyService.getAllCompany());
    }

    @PutMapping("/request/updateStatus/{requestId}")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Integer requestId){
        try{
            return ResponseEntity.ok(requestService.updateRequestStatus(requestId));
        } catch (Exception e) {
           return ResponseEntity.status(500).body("Failed to update request status.");
        }
    }
}
