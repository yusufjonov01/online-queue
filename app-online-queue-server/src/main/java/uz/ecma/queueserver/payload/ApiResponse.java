package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String messageUzb;
    private String messageEng;
    private String messageRus;
    private String messageUzK;
    private boolean success;
    private Object object;
    private Integer count;
    private String time;

    public ApiResponse(boolean success, Object object, Integer count) {
        this.success = success;
        this.object = object;
        this.count = count;
    }

    public ApiResponse(boolean success, Object object, String time) {
        this.success = success;
        this.object = object;
        this.time = time;
    }

    public ApiResponse(boolean success) {
        this.success = success;
    }

    public ApiResponse(String messageUzb, String messageEng, String messageRus, String messageUzK, boolean success) {
        this.messageUzb = messageUzb;
        this.messageEng = messageEng;
        this.messageRus = messageRus;
        this.messageUzK = messageUzK;
        this.success = success;
    }

    public ApiResponse(String messageUzb, String messageEng, String messageRus, String messageUzK, boolean success, Object object) {
        this.messageUzb = messageUzb;
        this.messageEng = messageEng;
        this.messageRus = messageRus;
        this.messageUzK = messageUzK;
        this.success = success;
        this.object = object;
    }

    public ApiResponse(boolean success, Object object) {
        this.success = success;
        this.object = object;
    }


    public ApiResponse(String messageUzb, Object object) {
        this.messageUzb = messageUzb;
        this.object = object;
    }
}
