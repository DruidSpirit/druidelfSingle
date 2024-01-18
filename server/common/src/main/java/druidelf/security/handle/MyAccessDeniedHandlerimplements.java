package druidelf.security.handle;


import druidelf.enums.ResponseDataEnums;
import druidelf.model.ResponseData;
import druidelf.util.UtilForHttpServletData;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限时处理机制
 */
@Log4j2
@Component("myAccessDeniedHandlerimplements")
public class MyAccessDeniedHandlerimplements implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {

        ResponseDataEnums responseFailDenied = ResponseDataEnums.RESPONSE_FAIL_DENIED;
        if ( e instanceof AuthorizationServiceException) {
            responseFailDenied = ResponseDataEnums.RESPONSE_FAIL_NO_PERMISSION;
       }

        UtilForHttpServletData.responseDataForHttp( httpServletResponse, ResponseData.FAILURE(responseFailDenied) );

    }
}
