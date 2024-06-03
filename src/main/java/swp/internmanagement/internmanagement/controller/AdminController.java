package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swp.internmanagement.internmanagement.payload.response.GetAllRequestResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllUserByParamResponse;
import swp.internmanagement.internmanagement.service.RequestService;
import swp.internmanagement.internmanagement.service.UserAccountService;

@RestController
@RequestMapping("/internbridge/admin")
public class AdminController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/request")
    public ResponseEntity<GetAllRequestResponse> getAllRequestResponse(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize)
    {
        return ResponseEntity.ok(requestService.getRequests(pageNo, pageSize));
    }

    @GetMapping("/search/param={param}")
    public ResponseEntity<GetAllUserByParamResponse> getAllUserByParamResponse(
            @PathVariable String param,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize)
    {
         return ResponseEntity.ok(userAccountService.getAllUserAccountsByParam(param, pageNo, pageSize));
    }
}
