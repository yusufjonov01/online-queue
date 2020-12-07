package uz.ecma.queueserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExistException extends RuntimeException {

    public ExistException(String message) {
        super(message);
    }

    public ExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
