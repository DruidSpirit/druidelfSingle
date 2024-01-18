package druidelf.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

public class UtilForHttpServletData {

    /**
     * 发送HTTP相应的json参数到请求页面
     * @param servletResponse HTTP响应
     * @param resData   发送的参数
     */
    public static void responseDataForHttp (ServletResponse servletResponse, Object resData ) throws IOException {

        servletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = servletResponse.getWriter();
        out.write(new ObjectMapper().writeValueAsString(resData));
        out.flush();
        out.close();
    }

    /**
     * 获取真实的ip
     */
    public static String getRealIp(HttpServletRequest request){

        // 有的user可能使用代理，为处理用户使用代理的情况，使用x-forwarded-for
        return request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for"):
               request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP"):
               request.getRemoteAddr();
    }
}
