package uz.ecma.queueserver.payload;

import lombok.Data;
import uz.ecma.queueserver.entity.AwareCompany;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class ReqContact {
    private UUID Id;
    private Integer districtId;
    private String address;
    private List<String> phoneNumbers;
    private String email;
    private String fax;
    private String lat;
    private List<AwareCompany> awareCompanies;
    private String lng;

}
