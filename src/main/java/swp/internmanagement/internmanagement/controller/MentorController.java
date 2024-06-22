package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllCourseByMentorIdResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllCourseInCompanyResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllTaskInCourseResponse;
import swp.internmanagement.internmanagement.service.CourseInternService;
import swp.internmanagement.internmanagement.service.CourseService;
import swp.internmanagement.internmanagement.service.InternTaskService;
import swp.internmanagement.internmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/internbridge/mentor")
public class MentorController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private InternTaskService internTaskService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseInternService courseInternService;

    // create task
    @PostMapping("/addactivities/{courseId}")
    public ResponseEntity<?> addActivities(@RequestBody CreateTaskRequest createTaskRequest,
            @PathVariable int courseId) {
        try {
            return new ResponseEntity<>(taskService.createTask(createTaskRequest, courseId), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/course/{mentorId}")
    public ResponseEntity<GetAllCourseByMentorIdResponse> getCourse(@PathVariable int mentorId) {
        return ResponseEntity.ok(courseService.getCourseByMentor(mentorId));
    }

    @GetMapping("/course/task/{courseId}&{mentorId}")
    public ResponseEntity<List<Task>> getCourseTask(@PathVariable int courseId, @PathVariable int mentorId) {
        return ResponseEntity.ok(taskService.getTasks(courseId, mentorId));
    }

    @GetMapping("/Allcourse/{mentorId}")
    public ResponseEntity<GetAllCourseInCompanyResponse> getCourseAll(
            @PathVariable int mentorId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize) {
        return ResponseEntity.ok(courseService.getCourseByMentorTable(mentorId, pageNo, pageSize));
    }

    @GetMapping("/task")
    public ResponseEntity<?> getAllTask(
        @RequestParam("user_id") Integer user_id,
        @RequestParam("company_id") Integer company_id,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ){
        return ResponseEntity.ok(courseService.getAllTaskInAllCourse(user_id,company_id,pageNo,pageSize));
    }
    @DeleteMapping("/task/delete/{taskId}")
    public ResponseEntity<?> deleteJob(@PathVariable int taskId) {
        try {
            boolean delete = taskService.deleteTask(taskId);
            if (delete) {
                return ResponseEntity.ok("Delete task successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete task.");
        }
        return ResponseEntity.status(500).body("task is not found.");
    }
    @PutMapping("task/update/{taskId}")
    public ResponseEntity<?> updateTask(@RequestBody CreateTaskRequest createTaskRequest,@PathVariable Integer taskId){
        try {
            return new ResponseEntity<>(taskService.updateTask(createTaskRequest, taskId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @GetMapping("course/internResult/{courseId}&{mentorId}")
    public ResponseEntity<?> getInternResult(@PathVariable int courseId, @PathVariable int mentorId) {
        try {
            return ResponseEntity.ok(courseInternService.getListInternResultFromCourse(courseId, mentorId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
