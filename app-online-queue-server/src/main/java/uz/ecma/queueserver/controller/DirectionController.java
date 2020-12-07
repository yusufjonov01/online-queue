package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.queueserver.entity.Direction;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.payload.ReqDirection;
import uz.ecma.queueserver.repository.DirectionRepository;
import uz.ecma.queueserver.repository.OperatorDirectionRepository;
import uz.ecma.queueserver.security.CurrentUser;
import uz.ecma.queueserver.service.DirectionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/direction")
public class DirectionController {
    @Autowired
    DirectionService directionService;

    @Autowired
    DirectionRepository directionRepository;
    @Autowired
    OperatorDirectionRepository operatorDirectionRepository;


    @PostMapping
    public HttpEntity<?> addDirection(@RequestBody ReqDirection reqDirection, @CurrentUser User user) {
        ApiResponse apiResponse = directionService.addDirection(reqDirection, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getDirection(@PathVariable Integer id, @CurrentUser User user) {
        Direction direction = directionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
        return ResponseEntity.ok(new ApiResponse(true, direction));
    }
    @GetMapping("companyDirection/{id}")
    public HttpEntity<?> getCompanyDirection(@PathVariable Integer id) {
        Direction direction = directionRepository.findByIdAndActive(id, true).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
        Integer count = operatorDirectionRepository.countByDirection_IdAndActive(id, true);
        return ResponseEntity.ok(new ApiResponse(true, direction, count));
    }

    @GetMapping("/byCompany/{id}")
    public HttpEntity<?> getDirectionByCompany(@PathVariable UUID id) {
        return ResponseEntity.ok(directionService.byCompany(id));
    }


    @PutMapping("/active/{id}")
    public HttpEntity<?> isActiveItem(@PathVariable Integer id, @RequestBody String requestType, @CurrentUser User user) {
        ApiResponse apiResponse = directionService.isActiveItem(id, requestType, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editDirection(@PathVariable Integer id, @RequestBody ReqDirection reqDirection, @CurrentUser User user) {
        ApiResponse apiResponse = directionService.editDirection(id, reqDirection, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteDirection(@PathVariable Integer id, @CurrentUser User user) {
        ApiResponse apiResponse = directionService.deleteDirection(id, user);
        return ResponseEntity.ok(apiResponse);
    }
}