package druidelf.security.handle;

import druidelf.enums.ResponseDataEnums;
import druidelf.model.ResponseData;
import druidelf.util.UtilForHttpServletData;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登入状态失败处理
 */
@Log4j2
@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {

        UtilForHttpServletData.responseDataForHttp( httpServletResponse, ResponseData.FAILURE(ResponseDataEnums.RESPONSE_FAIL_NOT_LOGIN));
    }
}
