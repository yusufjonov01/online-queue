package uz.ecma.queueserver.payload;

import lombok.Data;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Role;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
public class ReqUser {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String password;
    private String prePassword;
    private Set<Role> roles;
    private String role;
    private String oldPassword;
    private Integer directionId;
}
