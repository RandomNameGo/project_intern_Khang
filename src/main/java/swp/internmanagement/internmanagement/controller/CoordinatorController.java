package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.payload.request.CreateCourseRequest;
import swp.internmanagement.internmanagement.payload.response.GetUserInSameCompanyResponse;
import swp.internmanagement.internmanagement.service.CourseInternService;
import swp.internmanagement.internmanagement.service.CourseService;
import swp.internmanagement.internmanagement.service.UserAccountService;

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

    //show mentor and intern in the same company
    @GetMapping("search/{companyId}")
    public ResponseEntity<GetUserInSameCompanyResponse> search(
            @PathVariable int companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize) {
        return ResponseEntity.ok(userAccountService.getUserInSameCompany(companyId, pageNo, pageSize));
    }

    //create a course
    @PostMapping("createCourse/{companyId}")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest createCourseRequest, @PathVariable int companyId) {
        return new ResponseEntity<>(courseService.addCourse(createCourseRequest, companyId), HttpStatus.CREATED);
    }

    //add intern to course
    @PostMapping("addIntern/{courseId}")
    public ResponseEntity<String> addIntern(@RequestBody AddInternToCourseRequest addInternToCourseRequest, @PathVariable int courseId) {
        return  new ResponseEntity<>(courseInternService.addInternToCourse(addInternToCourseRequest, courseId), HttpStatus.CREATED);
    }

}
