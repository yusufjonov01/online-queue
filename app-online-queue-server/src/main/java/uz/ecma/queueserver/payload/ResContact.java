package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.AwareCompany;
import uz.ecma.queueserver.entity.District;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResContact {
    private District district;
    private String address;
    private List<String> phoneNumbers;
    private String email;
    private String fax;
    private String lat;
    private String lng;
    private List<AwareCompany> awareCompanies;
}
