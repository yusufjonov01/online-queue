package uz.ecma.queueserver.payload;

import lombok.Data;
import uz.ecma.queueserver.entity.AwareCompany;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class ReqCompany {
    private String name;
    private ReqContact reqContact;
    private UUID logoId;
    private ReqUser reqUser;
    private String tin;
    private Integer categoryId;
    private ReqWorkTime reqWorkTime;
    private boolean active;
    private String requestType;
}
