package swp.internmanagement.internmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateTaskRequest;
import swp.internmanagement.internmanagement.service.TaskService;

@RestController
@RequestMapping("/internbridge/mentor")
public class MentorController {

    @Autowired
    private TaskService taskService;


    @PostMapping("/addactivities/{courseId}")
    public ResponseEntity<Task> addActivities(@RequestBody CreateTaskRequest createTaskRequest, @PathVariable int courseId) {
        return new ResponseEntity<>(taskService.createTask(createTaskRequest, courseId), HttpStatus.CREATED);
    }
}
