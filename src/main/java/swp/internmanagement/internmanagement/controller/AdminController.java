package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateCompanyRequest;
import swp.internmanagement.internmanagement.payload.response.AcceptedJobApplicationResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserResponse;
import swp.internmanagement.internmanagement.service.CompanyService;
import swp.internmanagement.internmanagement.service.JobApplicationService;
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

    @GetMapping("/request")
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
    public ResponseEntity<GetAllUserResponse> getAllUserResponse(
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
    public ResponseEntity<?> createCompany(@RequestBody CreateCompanyRequest companyRequest) {
        try {
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

    @DeleteMapping("/userAccount/delete/id={userId}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable int userId) {
        return ResponseEntity.ok(userAccountService.deleteUserAccount(userId));
    }

    @DeleteMapping("/company/delete/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable int companyId) {
        return ResponseEntity.ok(companyService.deleteCompany(companyId));
    }

    @PutMapping("/company/update/{companyId}")
    public ResponseEntity<?> updateCompany(@PathVariable int companyId ,@RequestBody UpdateCompanyRequest companyRequest) {
        return ResponseEntity.ok(companyService.updateCompany(companyId, companyRequest));
    }
}
