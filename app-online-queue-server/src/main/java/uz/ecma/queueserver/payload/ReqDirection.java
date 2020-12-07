package uz.ecma.queueserver.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReqDirection {
    private Integer id;
    private String nameUzl;
    private String nameUzk;
    private String nameRu;
    private String nameEn;
    private boolean isAutoTime;
    private boolean active;
    private Integer time;
    private String requestType;
    private String directionDateName;
}
