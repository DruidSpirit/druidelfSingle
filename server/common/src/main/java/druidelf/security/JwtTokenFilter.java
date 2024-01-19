package druidelf.security;

import cn.hutool.core.util.StrUtil;
import druidelf.enums.ResponseDataEnums;
import druidelf.model.ResponseData;
import druidelf.util.UtilForHttpServletData;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * jwt的token校验过滤器
 */
public class JwtTokenFilter extends UsernamePasswordAuthenticationFilter {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final JwtTokenProvider jwtTokenProvider;
    private final String[] allowUrl;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider,AuthenticationManager authenticationManager,String... allowUrl ) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.allowUrl = allowUrl;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        for (String url : allowUrl) {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String requestURI = httpRequest.getRequestURI(); // 使用 getServletPath() 替代 getRequestURI()
            if(antPathMatcher.match(url, requestURI)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String token = resolveToken(request);

        if ( StrUtil.isBlank(token) ) {
            UtilForHttpServletData.responseDataForHttp( response, ResponseData.FAILURE(ResponseDataEnums.RESPONSE_FAIL_INVALID_TOKEN));
        }

        if ( !jwtTokenProvider.validateToken(token) ) {
            UtilForHttpServletData.responseDataForHttp( response, ResponseData.FAILURE(ResponseDataEnums.RESPONSE_FAIL_CHECK_TOKEN));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                jwtTokenProvider.getUsernameFromToken(token), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private String resolveToken(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);  // 去除 "Bearer " 前缀
        }

        return token;
    }
}
