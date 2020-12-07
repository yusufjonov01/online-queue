package uz.ecma.queueserver.payload;

import lombok.Data;

@Data
public class ReqLogin {
    private String phoneNumber;
    private String password;
}
