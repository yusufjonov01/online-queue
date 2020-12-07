package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.queueserver.entity.Complains;
import uz.ecma.queueserver.entity.Message;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.security.CurrentUser;
import uz.ecma.queueserver.service.MessageService;
import uz.ecma.queueserver.service.SupervisorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PutMapping("/{id}")
    public HttpEntity<?> changeMessageView(@PathVariable UUID id) {
        ApiResponse response = messageService.changeMessageView(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping
    public HttpEntity<?> getMessagesList(@CurrentUser User user) {
        System.out.println(user.getId());
        ApiResponse response = messageService.getMessages(user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/report/{id}")
    public HttpEntity<?> createReportFromUser(@PathVariable String id, @RequestBody String message) {
        ApiResponse response = messageService.createReportFromUser(id, message);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/crateComplain")
    public HttpEntity<?> crateComplain(@RequestBody List<String> names) {
        ApiResponse response = messageService.addComplain(names);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }
}
