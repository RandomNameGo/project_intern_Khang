package swp.internmanagement.internmanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateInternDetailRequest;
import swp.internmanagement.internmanagement.payload.response.JobApplicationResponse;
import swp.internmanagement.internmanagement.service.InternDetailService;
import swp.internmanagement.internmanagement.service.JobApplicationService;
import swp.internmanagement.internmanagement.service.RequestService;


@RestController
@RequestMapping("/internbridge/manager")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private InternDetailService internDetailService;

    @PostMapping("/postjob")
    public ResponseEntity<?>  PostRecruitment(
        @RequestParam("jobId") Integer jobId,
        @RequestParam("email") String email,
        @RequestParam("fullName") String fullName,
        @RequestParam("cv") MultipartFile cv
    ) {
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

    @GetMapping("/downLoadCV")
    public ResponseEntity<byte[]> downLoadCV(@RequestParam ("id") Integer id) {
        Optional<JobApplication> jobApplicationOptional = jobApplicationService.getJobApplicationById(id);
        if(jobApplicationOptional.isPresent()){
            JobApplication jobApplication = jobApplicationOptional.get();
            byte[] cv=jobApplication.getCV();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + jobApplication.getFullName() + "_CV.pdf");
            headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return new ResponseEntity<>(cv, headers, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/jobApplication")
    public ResponseEntity<JobApplicationResponse> getJobApplication
    (
        @RequestParam("companyid") int companyId,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize
    ){
        
        return ResponseEntity.ok(jobApplicationService.getAllJobApplication(pageNo, pageSize, companyId));
    }
    @PutMapping("/jobApplication/id={id}&status={status}")
    public String update(@PathVariable Integer id, @PathVariable Integer status){
        return jobApplicationService.updateJobApplication(id,status);
    }

    @PutMapping("/intern/internDetail/update/{interId}")
    public ResponseEntity<?> updateInternDetail(@RequestBody UpdateInternDetailRequest updateInternDetailRequest, @PathVariable Integer interId){
        return ResponseEntity.ok(internDetailService.updateInternDetail(updateInternDetailRequest,interId));
    }
}
