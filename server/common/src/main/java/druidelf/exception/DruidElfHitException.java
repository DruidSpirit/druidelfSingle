package druidelf.exception;

import druidelf.enums.ResponseInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class DruidElfHitException extends RuntimeException {

    private String message;
    private Throwable cause;
    private Integer code;

    public DruidElfHitException(String message) {
        super(message);
        this.message = message;
    }

    public DruidElfHitException(ResponseInterface responseInterface) {
        super(responseInterface.getName());
        this.message = responseInterface.getName();
        this.code = responseInterface.getStatusCode();
    }

    public DruidElfHitException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }


}
