package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.payload.ReqUser;
import uz.ecma.queueserver.payload.ResUser;
import uz.ecma.queueserver.repository.UserRepository;
import uz.ecma.queueserver.security.CurrentUser;
import uz.ecma.queueserver.service.AuthService;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/userMe")
    public HttpEntity<?> userMe(@CurrentUser User user) {
        return ResponseEntity.status(user == null ? 409 : 200).body(user);
    }

    @PutMapping("/editPassword")
    public HttpEntity<?> editModerator(@RequestBody ReqUser reqUser, @CurrentUser User user) {
        ApiResponse apiResponse = authService.editPassword(reqUser, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/byRole/{role_id}")
    public HttpEntity<?> getUsersByRole(@CurrentUser User user, @PathVariable Integer role_id) {
        List<User> userList = userRepository.getUsersByRole(role_id);
        List<List<User>> usersList = new ArrayList<>();
        usersList.add(userList);
        return ResponseEntity.status(userList.isEmpty() ? 409 : 200).body(usersList);
    }
}
