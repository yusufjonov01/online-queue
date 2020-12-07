package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ResToken {
    private String token;
    private String tokenType="Bearer ";

    public ResToken(String token) {
        this.token = token;
    }
}
