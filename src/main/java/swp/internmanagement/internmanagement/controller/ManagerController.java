package swp.internmanagement.internmanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.payload.request.PostJobApplicationRequest;
import swp.internmanagement.internmanagement.payload.response.JobApplicationResponse;
import swp.internmanagement.internmanagement.service.JobApplicationService;
import swp.internmanagement.internmanagement.service.RequestService;


@RestController
@RequestMapping("/internbridge/manager")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping("/postjob")
    public ResponseEntity<?>  PostRecruitment(
        @RequestParam("field_id") int field_id,
        @RequestParam("company_id") int company_id,
        @RequestParam("job_name") String job_name,
        @RequestParam("job_description") String job_description
    ) {
        PostJobApplicationRequest postJobApplicationRequest=new PostJobApplicationRequest(field_id,company_id,job_name,job_description);
        boolean result = jobApplicationService.postJobApplication(postJobApplicationRequest);
        String response="";
        if (result) {
            return ResponseEntity.ok("Post job submitted successfully.");
        } else {
            return ResponseEntity.status(500).body("Failed to post job.");
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
        try {
            return jobApplicationService.updateJobApplication(id,status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
