package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.entity.WorkTime;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.payload.ReqWorkTime;
import uz.ecma.queueserver.payload.ResWorkTime;
import uz.ecma.queueserver.repository.WorkTimeRepository;
import uz.ecma.queueserver.security.CurrentUser;
import uz.ecma.queueserver.service.WorkTimeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/workTime")
public class WorkTimeController {
    @Autowired
    WorkTimeService workTimeService;
    @Autowired
    WorkTimeRepository workTimeRepository;

    @PostMapping
    public HttpEntity<?> saveWorkTime(@RequestBody ReqWorkTime reqWorkTime, @CurrentUser User user) {
        ApiResponse apiResponse = workTimeService.addWorkTime(reqWorkTime, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editWorkTime(@PathVariable UUID id, @RequestBody ReqWorkTime reqWorkTime, @CurrentUser User user) {
        ApiResponse apiResponse = workTimeService.editWorkTime(id, reqWorkTime, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getWorkTime(@PathVariable UUID id) {
//        WorkTime workTime = workTimeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getWorkTime"));
        List<WorkTime> byCompany_idOrderByWeekName = workTimeRepository.findByCompany_IdOrderByWeekName(id);
        return ResponseEntity.ok(new ApiResponse(true, byCompany_idOrderByWeekName));
    }

    @GetMapping("/list/{id}")
    public HttpEntity<List<ResWorkTime>> getWorkTimeList(@PathVariable UUID id) {
        return ResponseEntity.ok(workTimeService.getWorkTimeList(id));
    }
}
