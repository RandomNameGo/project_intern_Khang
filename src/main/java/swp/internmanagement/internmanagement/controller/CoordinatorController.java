package swp.internmanagement.internmanagement.controller;

import java.util.List;
import java.util.Optional;

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
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.payload.request.AddScheduleRequest;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.request.FeedBackRequest;
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

    @Autowired
    private CoordinatorFeedbackToInternService coordinatorFeedbackToInternService;

    @Autowired
    private CourseFeedbackService courseFeedbackService;

    //show mentor and intern in the same company
    @GetMapping("search/{companyId}")
    public ResponseEntity<GetUserInSameCompanyResponse> search(
            @PathVariable int companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return ResponseEntity.ok(userAccountService.getUserInSameCompany(companyId, pageNo, pageSize));
    }

    //filter bt role
    //enter parameter role to filter

    @GetMapping("/search/filter/{companyId}")
    public ResponseEntity<GetAllUserByRoleResponse> getAllUsersByRole(
            @PathVariable int companyId,
            @RequestParam String role,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(userAccountService.getAllUserByRole(companyId, role, pageNo, pageSize));
    }

    //    public ResponseEntity<List<UserAccount>> getAllUserByRole(@PathVariable int companyId, @RequestParam String role) {
    //        return ResponseEntity.ok(userAccountService.getAllUserAccountByRole(companyId, role));
    //       }

    //create a course
    @PostMapping("createCourse/{companyId}")
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest createCourseRequest, @PathVariable int companyId) {
        try{
            return new ResponseEntity<>(courseService.addCourse(createCourseRequest, companyId), HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //add intern to course
    @PostMapping("/addIntern/{courseId}")
    public ResponseEntity<String> addIntern(@RequestBody List<AddInternToCourseRequest> addInternToCourseRequest, @PathVariable int courseId) {
        try {
            return  new ResponseEntity<>(courseInternService.addInternToCourse(addInternToCourseRequest, courseId), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @PostMapping("/jobApplication")
    public ResponseEntity<AcceptedJobApplicationResponse> getAllAcceptedJobApplication(
            @RequestParam("companyId") Integer companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return ResponseEntity.ok(jobApplicationService.getAllAcceptedJobApplicationById(companyId,pageNo, pageSize));
    }

    @PostMapping("/intern/sendFeedback/{coordinatorId}&{internId}")
    public ResponseEntity<?> sendFeedbackToIntern(@RequestBody FeedBackRequest feedBackRequest, @PathVariable int coordinatorId, @PathVariable int internId) {
        try{
            return new ResponseEntity<>(coordinatorFeedbackToInternService.sendFeedback(feedBackRequest, coordinatorId, internId), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/course/allIntern/{courseId}&{coordinatorId}")
    public ResponseEntity<?> getAllInternInCourse(@PathVariable int courseId, @PathVariable int coordinatorId) {
        try{
            return ResponseEntity.ok(courseInternService.getAllInternInCourseByCoordinator(courseId, coordinatorId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/feedback/course/{coordinatorId}")
    public ResponseEntity<?> getAllFeedbackCourse(@PathVariable int coordinatorId) {
        try {
            return ResponseEntity.of(Optional.ofNullable(courseService.getAllEndCourses(coordinatorId)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @GetMapping("/showSchedule/{companyId}")
    public ResponseEntity<?> showSchedule(
            @PathVariable Integer companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        try {
            return ResponseEntity.ok(interviewScheduleService.getAllSchedule(companyId, pageNo, pageSize));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @DeleteMapping("/deleteSchedule/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Integer scheduleId){
        try {
            return ResponseEntity.ok(interviewScheduleService.deleteSchedule(scheduleId));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/course/feedback/{courseId}&{coordinatorId}")
    public ResponseEntity<?> getCourseFeedback(@PathVariable int courseId, @PathVariable int coordinatorId) {
        try{
            return ResponseEntity.ok(courseFeedbackService.getAllCourseFeedback(courseId,coordinatorId));
        }catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
