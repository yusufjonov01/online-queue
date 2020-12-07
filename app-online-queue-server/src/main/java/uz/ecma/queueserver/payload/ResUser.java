package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Role;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUser {
    private String firstName;
    private String lastName;
    private String middleName;
    private Company company;
    private List<Role> roles;
    private String phoneNumber;
}
