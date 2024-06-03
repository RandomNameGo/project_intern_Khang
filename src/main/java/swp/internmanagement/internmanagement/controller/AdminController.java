package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;
import swp.internmanagement.internmanagement.service.RequestService;

@RestController
@RequestMapping("/internbridge/admin")
public class AdminController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/request")
    public ResponseEntity<GetAllRequestResponse> getAllRequestResponse(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize)
    {
        return ResponseEntity.ok(requestService.getRequests(pageNo, pageSize));
    }
}
