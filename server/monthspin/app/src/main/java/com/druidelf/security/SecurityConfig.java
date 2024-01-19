package com.druidelf.security;

import druidelf.security.IpVisitFilter;
import druidelf.security.JwtTokenFilter;
import druidelf.security.JwtTokenProvider;
import druidelf.security.handle.CustomAuthenticationEntryPoint;
import druidelf.security.handle.MyAccessDeniedHandlerimplements;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Data
@Configuration
@EnableWebSecurity
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties("web-security-config")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String loginSubmitUrl;
    private String registerSubmitUrl;

    private final MyAccessDeniedHandlerimplements myAccessDeniedHandlerimplements;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * ip频繁访问过滤器
     */
    private final IpVisitFilter ipVisitFilter;

    /**
     * 浏览器跳转配置
     * @param http secutity 中的网络请求对象
     * @throws Exception  配置异常
     */
    @SuppressWarnings("SpringElInspection")
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //  允许未认证访问的路径
        String[] allowUrl = {loginSubmitUrl,registerSubmitUrl};
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers(allowUrl).anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .access("@rbacPermission.hasPermission(request, authentication)")
        ;
        // 配置认证过滤器
        http
                .addFilterBefore(ipVisitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 配置异常处理器
        http
                .exceptionHandling()
                // 配置认证失败处理器
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                // 配置授权失败处理器
                .accessDeniedHandler(myAccessDeniedHandlerimplements)
        ;
        //允许跨域
        http.cors();

    }

    /**
     * 用户密码加密配置
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() throws Exception {
        return new JwtTokenFilter(jwtTokenProvider,authenticationManagerBean(),loginSubmitUrl,registerSubmitUrl);
    }

}
