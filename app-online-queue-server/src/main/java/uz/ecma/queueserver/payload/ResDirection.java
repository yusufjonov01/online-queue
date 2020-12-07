package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResDirection {
    private Integer id;

    private String nameUzl;
    private String nameUzk;
    private String nameRu;
    private String nameEn;
    private boolean isAutoTime;
    private Integer time;

    private List<WorkTime> workTimes;

    private boolean active = false;

    private Double rate;

}
