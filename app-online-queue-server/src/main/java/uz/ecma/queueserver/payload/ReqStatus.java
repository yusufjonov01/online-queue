package uz.ecma.queueserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ReqStatus {
    private UUID queueId;
    private String status;
}
