package druidelf.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class DruidElfSystemException extends RuntimeException {

    private String message;
    private Throwable cause;

    public DruidElfSystemException(String message) {
        super(message);
        this.message = message;
    }

    public DruidElfSystemException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }
}
