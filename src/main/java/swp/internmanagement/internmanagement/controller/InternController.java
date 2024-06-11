package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swp.internmanagement.internmanagement.entity.CourseIntern;
import swp.internmanagement.internmanagement.service.CourseInternService;

import java.util.List;

@RestController
@RequestMapping("/internbridge/intern")
@CrossOrigin(origins = "http://localhost:3000")
public class InternController {
    @Autowired
    private CourseInternService courseInternService;

    @GetMapping("/allCourse/{internId}")
    public ResponseEntity<List<CourseIntern>> getCourse(@PathVariable int internId) {
        return ResponseEntity.ok(courseInternService.geCoursesByInternId(internId));
    }
}
