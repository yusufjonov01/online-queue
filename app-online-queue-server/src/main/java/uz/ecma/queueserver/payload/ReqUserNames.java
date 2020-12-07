package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUserNames {
    private String firstName;
    private String lastName;
    private String middleName;
}