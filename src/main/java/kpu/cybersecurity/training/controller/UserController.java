package kpu.cybersecurity.training.controller;

import kpu.cybersecurity.training.domain.dto.request.ReqUserDTO;
import kpu.cybersecurity.training.domain.dto.response.ResUserDTO;
import kpu.cybersecurity.training.service.UserService;
import kpu.cybersecurity.training.util.annotation.ApiMessage;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @ApiMessage("Create User")
    public ResponseEntity<ResUserDTO> createUser(@RequestBody ReqUserDTO dto) throws UniqueException {
        ResUserDTO createdUser = userService.createUser(dto);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/get/{userId}")
    @ApiMessage("Get User by User ID")
    public ResponseEntity<ResUserDTO> getUserByUserId(@PathVariable("userId") String userId) throws UniqueException {
        ResUserDTO user = userService.getUserDTOByUserID(userId);
        return ResponseEntity.ok(user);
    }
}
