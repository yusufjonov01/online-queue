package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Direction;
import uz.ecma.queueserver.entity.Role;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResOperatorDirection {
    private UUID operatorDirectionId;
    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private boolean operatorActive;
    private boolean enabled;
    private Double rateAmount;
    private int countRate;
    private Timestamp createdAt;
    private UUID updateBy;
    private Timestamp updatedAt;
    private UUID companyId;
    private List<Role> roles;
    private Direction direction;

}
