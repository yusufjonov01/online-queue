package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResQueue {
    private UUID id;
    private String userPhoneNumber;
    private UUID operatorId;
    private String status;
    private Integer queueNumber;
    private String probablyTime;
    private String startTime;
    private String finishTime;
    private Integer countOperator;
    private String lastQueueTime;
    private Integer directionId;
    private Integer countAllQueue;
    private String durationTime;

    public ResQueue(UUID id, String userPhoneNumber, UUID operatorId, String status, Integer queueNumber, String startTime, String finishTime, Integer directionId) {
        this.id = id;
        this.userPhoneNumber = userPhoneNumber;
        this.operatorId = operatorId;
        this.status = status;
        this.queueNumber = queueNumber;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.directionId = directionId;
    }

    private Integer lastQueueNumber;

    public ResQueue(Integer directionId, Integer countAllQueue, String durationTime, Integer lastQueueNumber) {
        this.directionId = directionId;
        this.countAllQueue = countAllQueue;
        this.durationTime = durationTime;
        this.lastQueueNumber = lastQueueNumber;
    }

    public ResQueue(Integer directionId,Integer queueNumber, Integer countAllQueue, String durationTime, Integer lastQueueNumber, String probablyTime) {
        this.directionId = directionId;
        this.queueNumber = queueNumber;
        this.countAllQueue = countAllQueue;
        this.durationTime = durationTime;
        this.lastQueueNumber = lastQueueNumber;
        this.probablyTime = probablyTime;
    }

    public ResQueue(Integer countAllQueue, Integer countOperator, String lastQueueTime, String durationTime) {
        this.countAllQueue = countAllQueue;
        this.countOperator = countOperator;
        this.lastQueueTime = lastQueueTime;
        this.durationTime = durationTime;
    }

    public ResQueue(UUID id, String userPhoneNumber, String durationTime, Integer countOperator, Integer directionId, UUID operatorId, String status, Integer queueNumber, String probablyTime, String startTime, String finishTime) {
        this.id = id;
        this.durationTime = durationTime;
        this.countOperator = countOperator;
        this.userPhoneNumber = userPhoneNumber;
        this.directionId = directionId;
        this.operatorId = operatorId;
        this.status = status;
        this.queueNumber = queueNumber;
        this.probablyTime = probablyTime;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public ResQueue(Integer countAllQueue, UUID id, String userPhoneNumber, String durationTime, Integer countOperator, Integer directionId, UUID operatorId, String status, Integer queueNumber, String probablyTime, String startTime, String finishTime) {
        this.countAllQueue = countAllQueue;
        this.id = id;
        this.durationTime = durationTime;
        this.countOperator = countOperator;
        this.userPhoneNumber = userPhoneNumber;
        this.directionId = directionId;
        this.operatorId = operatorId;
        this.status = status;
        this.queueNumber = queueNumber;
        this.probablyTime = probablyTime;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}
