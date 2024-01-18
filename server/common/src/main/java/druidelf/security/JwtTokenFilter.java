package druidelf.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider,AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    jwtTokenProvider.getUsernameFromToken(token), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

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
