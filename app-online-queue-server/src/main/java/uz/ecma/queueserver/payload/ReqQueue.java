package uz.ecma.queueserver.payload;

import lombok.Data;

@Data
public class ReqQueue {
    private String phoneNumber;
    private Integer directionId;
}