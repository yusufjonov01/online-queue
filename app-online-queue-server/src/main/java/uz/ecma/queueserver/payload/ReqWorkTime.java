package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class ReqWorkTime {
    private String startTime;
    private String finishTime;
    private boolean lunchActive;
    private String lunchStartTime;
    private String lunchFinishTime;
    private UUID companyId;
    private Integer directionId;
    private String weekDay;
    private boolean active;
}
