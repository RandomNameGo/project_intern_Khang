package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.payload.response.GetAllTaskInCourseResponse;
import swp.internmanagement.internmanagement.payload.response.GetCourseNameResponse;
import swp.internmanagement.internmanagement.service.CourseInternService;
import swp.internmanagement.internmanagement.service.CourseService;
import swp.internmanagement.internmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/internbridge/intern")
@CrossOrigin(origins = "http://localhost:3000")
public class InternController {

    @Autowired
    private CourseInternService courseInternService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CourseService courseService;

    //Show all course intern attended
    @GetMapping("/allCourse/{internId}")
    public ResponseEntity<List<CourseIntern>> getCourse(@PathVariable int internId) {
        return ResponseEntity.ok(courseInternService.geCoursesByInternId(internId));
    }

    @GetMapping("/course/task/{courseId}")
    public ResponseEntity<GetAllTaskInCourseResponse> getAllTaskInCourse(
            @PathVariable int courseId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize
    ) {
        return ResponseEntity.ok(taskService.getTasks(courseId, pageNo, pageSize));
    }

    @GetMapping("/courseName/{courseId}")
    public ResponseEntity<GetCourseNameResponse> getCourseName(@PathVariable int courseId) {
        return ResponseEntity.ok(courseService.getCourseName(courseId));
    }
}
