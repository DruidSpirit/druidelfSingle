package druidelf.exception;

import druidelf.enums.ResponseDataEnums;
import druidelf.model.ResponseData;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@ControllerAdvice
public class ControllerExceptionHandle {

    /**
     * 参数校验异常
     * @param ex 传参异常
     * @return  铺抓到的异常信息
     */
    @ResponseBody
    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseData<Map<String,String>> errorHandler(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> map = new HashMap<>();
        for (FieldError fieldError:bindingResult.getFieldErrors()) {
            map.put( fieldError.getDefaultMessage(),fieldError.getField() );
        }

        return ResponseData.FAILURE(map);
    }

    /**
     * 系统自定义异常(系统异常)
     * @param druidElfSystemException   系统自定义异常
     * @return  ResponseData            前端通用返回体
     */
    @ResponseBody
    @ExceptionHandler( DruidElfSystemException.class )
    public ResponseData<Object> druidElfSystemExceptionHandle (DruidElfSystemException druidElfSystemException) {

        log.error(druidElfSystemException.getMessage(),druidElfSystemException);
        return ResponseData.FAILURE(ResponseDataEnums.RESPONSE_SYSTEM_ERROR);
    }

    /**
     * 系统自定义异常(提示异常)
     * @param druidElfHitException   系统自定义异常
     * @return  ResponseData            前端通用返回体
     */
    @ResponseBody
    @ExceptionHandler( DruidElfHitException.class )
    public ResponseData<Object> druidElfHitExceptionHandle (DruidElfHitException druidElfHitException) {

        log.error(druidElfHitException.getMessage(),druidElfHitException);
        ResponseData<Object> responseData = new ResponseData<>();
        responseData.setStatus(false);
        responseData.setMessage(druidElfHitException.getMessage());
        responseData.setStatusCode(druidElfHitException.getCode());
        return responseData;
    }
}
