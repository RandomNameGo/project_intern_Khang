package swp.internmanagement.internmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import swp.internmanagement.internmanagement.entity.Field;
import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.entity.Request;
// import swp.internmanagement.internmanagement.payload.request.HelpRequest;
// import swp.internmanagement.internmanagement.service.JobService;
// import swp.internmanagement.internmanagement.service.RequestService;
import swp.internmanagement.internmanagement.payload.request.HelpRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllFieldsResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllJobsResponse;
import swp.internmanagement.internmanagement.payload.response.SearchJobsResponse;
import swp.internmanagement.internmanagement.service.FieldService;
import swp.internmanagement.internmanagement.service.JobService;
import swp.internmanagement.internmanagement.service.RequestService;
import swp.internmanagement.internmanagement.service.UserAccountService;

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

    @GetMapping("/jobs")
    public ResponseEntity<GetAllJobsResponse> getAllJobs(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(jobService.getAllJobs(pageNo, pageSize));
    }

    // @GetMapping("/jobs/id={id}")
    // public ResponseEntity<SearchJobsResponse> getJobID(
    //         @PathVariable Integer id,
    //         @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
    //         @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
    //     return ResponseEntity.ok(jobService.getJobById(id, pageNo, pageSize));
    // }
    @GetMapping("/jobs/id={id}")
    public ResponseEntity<Job> getJobId( @PathVariable Integer id) {
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
    public ResponseEntity<Request> sendRequest(@RequestBody HelpRequest helpRequest) {
        return new ResponseEntity<>(requestService.saveRequest(helpRequest), HttpStatus.CREATED);
    }
}