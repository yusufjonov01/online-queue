package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Direction;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResWorkTime {
    private UUID id;
    private String startTime;
    private String finishTime;
    private boolean lunchActive;
    private String lunchStartTime;
    private String lunchFinishTime;
    private Company companyId;
    private Direction direction;
    private boolean  active;
}
