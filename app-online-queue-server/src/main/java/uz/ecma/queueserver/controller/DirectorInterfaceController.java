package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.*;
import uz.ecma.queueserver.repository.OperatorDirectionRepository;
import uz.ecma.queueserver.security.CurrentUser;
import uz.ecma.queueserver.service.DirectorInterfaceService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/directorInterface")
public class DirectorInterfaceController {

    @Autowired
    DirectorInterfaceService directorInterfaceService;
    @Autowired
    OperatorDirectionRepository operatorDirectionRepository;

    @PostMapping
    public HttpEntity<?> addStaff(@RequestBody ReqUser reqUser, @CurrentUser User user) {
        return ResponseEntity.ok(directorInterfaceService.addStaff(reqUser, user));
    }

    @PostMapping("/worktime")
    public HttpEntity<?> addStaff(@RequestBody List<ReqWorkTime> workTimes, @CurrentUser User user) {
        ApiResponse apiResponse = directorInterfaceService.addWorkTime(workTimes, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editStaff(@PathVariable UUID id, @RequestBody ReqUser reqUser, @CurrentUser User user) {
        ApiResponse apiResponse = directorInterfaceService.editStaff(reqUser, user, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getListStaff/{id}")
    public HttpEntity<?> getStaffListOperator(@PathVariable String id, @CurrentUser User user) {
        List<User> listStaff = directorInterfaceService.getListStaff(user, id);
        return ResponseEntity.ok(listStaff);
    }

    @GetMapping("getOperatorList/{id}")
    public HttpEntity<?> getOperatorList(@PathVariable UUID id) {
        ApiResponse operatorListRes = directorInterfaceService.getOperatorListRes(id);
        return ResponseEntity.status(operatorListRes.isSuccess() ? 200 : 409).body(operatorListRes);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getStaff(@PathVariable UUID id) {
        return ResponseEntity.ok(directorInterfaceService.getStaff(directorInterfaceService.getUser(id)));
    }

    @PutMapping("active/{id}")
    public HttpEntity<?> isActiveStaff(@PathVariable UUID id, @RequestBody String requestType, @CurrentUser User user) {
        ApiResponse activeStaff = directorInterfaceService.isActiveStaff(id, requestType, user);
        return ResponseEntity.status(activeStaff.isSuccess() ? 200 : 401).body(activeStaff);
    }

    @PutMapping("onOffOperator/{id}")
    public HttpEntity<?> onOffOperator(@PathVariable UUID id, boolean active, @CurrentUser User user) {
        ApiResponse apiResponse = directorInterfaceService.onOffOperator(id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteStaff(@PathVariable UUID id, @CurrentUser User user) {
        ApiResponse apiResponse = directorInterfaceService.deleteStaff(id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }
}