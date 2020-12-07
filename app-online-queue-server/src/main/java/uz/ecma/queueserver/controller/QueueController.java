package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.queueserver.entity.OperatorDirection;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.*;
import uz.ecma.queueserver.repository.OperatorDirectionRepository;
import uz.ecma.queueserver.repository.UserRepository;
import uz.ecma.queueserver.security.CurrentUser;
import uz.ecma.queueserver.service.CheckRole;
import uz.ecma.queueserver.service.QueueService;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/queue")
public class
QueueController {
    final
    QueueService queueService;
    final
    UserRepository userRepository;
    final
    CheckRole checkRole;
    final
    OperatorDirectionRepository operatorDirectionRepository;

    public QueueController(QueueService queueService, UserRepository userRepository,
                           CheckRole checkRole, OperatorDirectionRepository operatorDirectionRepository) {
        this.queueService = queueService;
        this.userRepository = userRepository;
        this.checkRole = checkRole;
        this.operatorDirectionRepository = operatorDirectionRepository;
    }

    @GetMapping("/list/{directionId}")
    public HttpEntity<?> getQueueByDirection(
            @PathVariable Integer directionId,
            @RequestParam(value = "status", defaultValue = "DEFAULT") String status,
            @RequestParam(value = "size", defaultValue = "10") Integer size, @CurrentUser User user
    ) {
        if (checkRole.isOperator(user)) {
            ResPageable resPageable = queueService.getQueuesByDirection(directionId, size, status);
            return ResponseEntity.ok(resPageable);
        }
        return ResponseEntity.status(409).body(false);
    }

    @GetMapping("/queueList")
    public HttpEntity<?> getQueueList(
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @CurrentUser User user) {
        if (checkRole.isDirector(user) || checkRole.isReception(user) || checkRole.isOperator(user)) {
            ApiResponse queueList = queueService.getQueueList(status.toUpperCase(), page, size, user);
            return ResponseEntity.status(queueList.isSuccess() ? 200 : 409).body(queueList);
        }
        return ResponseEntity.ok(new ApiResponse(false, null));
    }


    @GetMapping("/getOperatorDirection/{id}")
    public HttpEntity<?> getOperatorDirection(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        OperatorDirection operatorDirection = operatorDirectionRepository.findByUserId(uuid).orElseThrow(() -> new ResourceNotFoundException("getOperatorDirection"));
        return ResponseEntity.status(operatorDirection.getId() == null ? 409 : 200).body(operatorDirection);

    }

    @PostMapping("/setStatus")
    public HttpEntity<?> setStatus(@RequestBody ReqStatus request, @CurrentUser User user) {
        ApiResponse response = queueService.setStatus(request.getQueueId(), request.getStatus(), user);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @PostMapping
    public HttpEntity<?> addQueue(@RequestBody ReqQueue request, @CurrentUser User user) {
        ApiResponse response = queueService.addQueue(request, user);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }
    @GetMapping("/verifyPhoneNumber")
    public HttpEntity<?> verifyPhoneNumber(
            @RequestParam(value = "phoneNumber", defaultValue = "") String phoneNumber,
            @RequestParam(value = "directionId", defaultValue = "") Integer directionId
    ) {
        ApiResponse phoneNumberHave = queueService.isPhoneNumberHave(phoneNumber, directionId);
        return ResponseEntity.status(phoneNumberHave.isSuccess() ? 200 : 409).body(phoneNumberHave);
    }

    @PutMapping("/getQueue/{statusType}")
    public HttpEntity<?> getQueueAccepted(@PathVariable String statusType, @CurrentUser User user) {
        ApiResponse queue = queueService.getQueueAccepted(statusType, user);
        return ResponseEntity.status(queue.isSuccess() ? 200 : 409).body(queue);
    }

    @GetMapping
    public HttpEntity<?> getQueue(@CurrentUser User user) {
        ResQueue queue = queueService.getQueue(user);
        return ResponseEntity.ok(queue);
    }

    @GetMapping("/getClientQueue/{id}")
    public HttpEntity<?> getClientQueue(@PathVariable Integer id,
//                                        @RequestParam(value = "status", defaultValue = "waiting") String status,
                                        @CurrentUser User user) {
        ApiResponse clientQueue = queueService.getClientQueue(id, user);
        return ResponseEntity.status(clientQueue.isSuccess() ? 200 : 409).body(clientQueue);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getQueueForClient(@PathVariable Integer id, @CurrentUser User user) {
        ResQueue queue = queueService.getQueueForClient(id);
        return ResponseEntity.ok(queue);
    }


}
