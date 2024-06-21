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

    //create task
    @PostMapping("/addactivities/{courseId}")
    public ResponseEntity<Task> addActivities(@RequestBody CreateTaskRequest createTaskRequest, @PathVariable int courseId) {
        Task tsk = taskService.createTask(createTaskRequest, courseId);
        internTaskService.addInternToTask(tsk);
        return new ResponseEntity<>(tsk, HttpStatus.CREATED);
    }

    @GetMapping("/course/{mentorId}")
    public ResponseEntity<GetAllCourseByMentorIdResponse> getCourse(@PathVariable int mentorId) {
        return ResponseEntity.ok(courseService.getCourseByMentor(mentorId));
    }

    @GetMapping("/course/task/{courseId}&{mentorId}")
    public ResponseEntity<List<Task>> getCourseTask(@PathVariable int courseId, @PathVariable int mentorId){
        return ResponseEntity.ok(taskService.getTasks(courseId, mentorId));
    }
    @GetMapping("/Allcourse/{mentorId}")
     public ResponseEntity<GetAllCourseInCompanyResponse> getCourseAll(
            @PathVariable int mentorId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getCourseByMentorTable(mentorId, pageNo, pageSize));
    }
}
