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
import org.springframework.web.bind.annotation.RestController;

import swp.internmanagement.internmanagement.entity.InternTask;
import swp.internmanagement.internmanagement.payload.request.SendCourseFeedbackRequest;
import swp.internmanagement.internmanagement.payload.response.CourseInternResponse;
import swp.internmanagement.internmanagement.payload.response.GetCourseNameResponse;
import swp.internmanagement.internmanagement.payload.response.ShowInternTaskResponse;
import swp.internmanagement.internmanagement.service.CoordinatorFeedbackToInternService;
import swp.internmanagement.internmanagement.service.CourseFeedbackService;
import swp.internmanagement.internmanagement.service.CourseInternService;
import swp.internmanagement.internmanagement.service.CourseService;
import swp.internmanagement.internmanagement.service.InternDetailService;
import swp.internmanagement.internmanagement.service.InternTaskService;
import swp.internmanagement.internmanagement.service.MentorFeedbackInternService;

@RestController
@RequestMapping("/internbridge/intern")
@CrossOrigin(origins = "http://localhost:3000")
public class InternController {

    @Autowired
    private CourseInternService courseInternService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MentorFeedbackInternService mentorFeedbackInternService;

    @Autowired
    private InternTaskService internTaskService;

    @Autowired
    private CoordinatorFeedbackToInternService coordinatorFeedbackToInternService;

    @Autowired
    private InternDetailService internDetailService;

    @Autowired
    private CourseFeedbackService courseFeedbackService;

    //Show all course intern attended
    @GetMapping("/allCourse/{internId}")
    public ResponseEntity<List<CourseInternResponse>> getCourse(@PathVariable int internId) {
        return ResponseEntity.ok(courseInternService.geCoursesByInternId(internId));
    }

    @GetMapping("/course/task/{courseId}&{internId}")
    public ResponseEntity<ShowInternTaskResponse> getTask(@PathVariable int courseId, @PathVariable int internId) {
        return ResponseEntity.ok(internTaskService.getInternTaskByCourseId(courseId, internId));
    }

    @GetMapping("/courseName/{courseId}&{internID}")
    public ResponseEntity<GetCourseNameResponse> getCourseName(@PathVariable int courseId, @PathVariable int internID) {
        return ResponseEntity.ok(courseService.getCourseName(courseId, internID));
    }

    @GetMapping("/getAllFeedback/{internId}")
    public ResponseEntity<?> getAllFeedback(@PathVariable int internId) {
        return ResponseEntity.ok(mentorFeedbackInternService.showAllFeedbackFromMentorResponse(internId));
    }

    @GetMapping("/getAllFeedback/coordinator/{internId}")
    public ResponseEntity<?> getAllFeedbackCoordinator(@PathVariable int internId) {
        try{
            return ResponseEntity.ok(coordinatorFeedbackToInternService.getFeedback(internId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/course/task/done/{taskId}&{internId}")
    public ResponseEntity<?> completeTask(@PathVariable int taskId, @PathVariable int internId) {
        InternTask internTask = new InternTask();
        internTask = internTaskService.getInternTaskByInternId(internId,taskId);
        int courseId = internTask.getTask().getCourse().getId();
        internTaskService.updateInternTask(taskId, internId);
        courseInternService.updateResult(internId, courseId);
        return new ResponseEntity<>(internTaskService.updateInternTask(taskId, internId), HttpStatus.OK);
    }

    @GetMapping("/detail/{internId}")
    public ResponseEntity<?> getDetail(@PathVariable int internId) {
        try {
            return ResponseEntity.ok(internDetailService.getInternDetail(internId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/course/verify/{internId}&{courseId}")
    public ResponseEntity<?> verifyCourse(@PathVariable int internId, @PathVariable int courseId) {
        return ResponseEntity.ok(courseInternService.verifyCourseIntern(internId, courseId));
    }

    @GetMapping("/course/endedCourse/{internId}")
    public ResponseEntity<?> getEndedCourse(@PathVariable int internId) {
        try {
            return ResponseEntity.ok(courseInternService.getListEndedCourseByIntern(internId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/course/feedback/{internId}&{courseId}")
    public ResponseEntity<?> feedback(@RequestBody SendCourseFeedbackRequest sendCourseFeedbackRequest,
                                      @PathVariable int internId, @PathVariable int courseId) {
        try{
            return new ResponseEntity<>(courseFeedbackService.sendCourseFeedback(sendCourseFeedbackRequest, internId, courseId), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/course/feedback/verify/{internId}&{courseId}")
    public ResponseEntity<?> verifyCourseFeedback(@PathVariable int internId, @PathVariable int courseId) {
        try{
            return ResponseEntity.ok(courseFeedbackService.verifyCourseFeedback(internId, courseId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
