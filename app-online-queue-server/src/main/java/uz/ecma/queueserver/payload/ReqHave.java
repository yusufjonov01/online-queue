package uz.ecma.queueserver.payload;

import lombok.Data;

@Data
public class ReqHave {
    private String contentType;
    private String phoneNumber;
    private String tin;
    private String companyName;
}
