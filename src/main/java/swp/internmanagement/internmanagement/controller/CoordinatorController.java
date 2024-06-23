package swp.internmanagement.internmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.payload.request.AddScheduleRequest;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.response.*;
import swp.internmanagement.internmanagement.service.*;

@RestController
@RequestMapping("/internbridge/coordinator")
@CrossOrigin(origins = "http://localhost:3000")
public class CoordinatorController {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseInternService courseInternService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    //show mentor and intern in the same company
    @GetMapping("search/{companyId}")
    public ResponseEntity<GetUserInSameCompanyResponse> search(
            @PathVariable int companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(userAccountService.getUserInSameCompany(companyId, pageNo, pageSize));
    }

    //filter bt role
    //enter parameter role to filter
    @GetMapping("/search/filter/{companyId}")
    public ResponseEntity<GetAllUserByRoleResponse> getUsersByRole(
            @PathVariable int companyId,
            @RequestParam String role,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(userAccountService.getAllUserByRole(companyId, role, pageNo, pageSize));
    }
//    public ResponseEntity<List<UserAccount>> getAllUserByRole(@PathVariable int companyId, @RequestParam String role) {
//        return ResponseEntity.ok(userAccountService.getAllUserAccountByRole(companyId, role));
//    }

    //create a course
    @PostMapping("createCourse/{companyId}")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest createCourseRequest, @PathVariable int companyId) {
        return new ResponseEntity<>(courseService.addCourse(createCourseRequest, companyId), HttpStatus.CREATED);
    }

    //add intern to course
    @PostMapping("addIntern/{courseId}")
    public ResponseEntity<String> addIntern(@RequestBody List<AddInternToCourseRequest> addInternToCourseRequest, @PathVariable int courseId) {
        return  new ResponseEntity<>(courseInternService.addInternToCourse(addInternToCourseRequest, courseId), HttpStatus.CREATED);
    }

    //show course detail
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Course> getUserInSameCompany(@PathVariable int courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }

    @DeleteMapping("/course/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable int courseId) {
        return new ResponseEntity<>(courseService.deleteCourse(courseId), HttpStatus.OK);
    }

    @PostMapping("/schedule/create")
    public ResponseEntity<?> addSchedule(@RequestBody AddScheduleRequest addScheduleRequest) {
        try{
            return new ResponseEntity<>(interviewScheduleService.addSchedule(addScheduleRequest), HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/course/companyId={companyId}")
    public ResponseEntity<GetAllCourseInCompanyResponse> getCourseByCompany(
            @PathVariable int companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getAllCourseInCompanyResponse(companyId, pageNo, pageSize));
    }

    @GetMapping("/company/mentor/{companyId}")
    public ResponseEntity<List<UserInfoResponse>> getMentorsInSameCompany(@PathVariable int companyId) {
        return ResponseEntity.ok(userAccountService.getAllMentor(companyId));
    }

    @GetMapping("company/intern/result/{companyId}")
    public ResponseEntity<?> internResult(@PathVariable int companyId) {
        return ResponseEntity.ok(userAccountService.getListAllInternResult(companyId));
    }
    @GetMapping("/jobApplication")
    public ResponseEntity<AcceptedJobApplicationResponse> getAllAcceptedJobApplication(
            @RequestParam("companyId") Integer companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return ResponseEntity.ok(jobApplicationService.getAllAcceptedJobApplicationById(companyId,pageNo, pageSize));
    }

}
