package druidelf.security;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@Configuration
@EnableWebSecurity
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties("web-security-config")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 跨域设置
     */
    private String[] origins;

    /**
     * 前端主页
     */
    private String frontHomeUrl;

    /**
     * 前端登入页面地址
     */
    private String loginPageUrl;

    /**
     * 登入页面表单提交地址
     */
    private String loginSubmitUrl;

    private final MyAccessDeniedHandlerimplements myAccessDeniedHandlerimplements;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

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

        //  放行静态资源
        String[] strings = {"/static/**","/static/picFile/**"};
        //  允许未认证访问的路径
        String[] allowUrl = {loginSubmitUrl};
        http
                .headers().frameOptions().disable()
                .and()
                .addFilterAfter(ipVisitFilter, BasicAuthenticationFilter.class)
                .logout()
                .logoutUrl("/ceUser/logout")
                .logoutSuccessUrl(frontHomeUrl)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .mvcMatchers(strings).permitAll()
                .antMatchers(allowUrl).permitAll()
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() //  解决二次跨域问题
                .anyRequest()
                .access("@rbacPermission.hasPermission(request, authentication)")   //  自定义授权机制
                .and()
                .exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandlerimplements)   //  无权限时异常处理
                .and()
                .csrf()
                .disable()  //  关掉csrf 防止post不能使用(这里使用jwt的令牌形式同样能保证安全)
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)// 关闭重定向
                .and()
                .formLogin()
                .loginPage(loginPageUrl)                                                  //  定义当需要用户登录时候，转到的登录页面。
                .loginProcessingUrl(loginSubmitUrl)
                .permitAll()  //  认证失败时处理机制
        ;

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
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("PUT", "POST", "GET", "OPTIONS", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.stream(origins).collect(Collectors.toList()));
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader( "Access-Token");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
