package com.druidelf.service.impl;


import com.druidelf.request.LoginRequest;
import com.druidelf.response.LoginResponse;
import com.druidelf.service.UserService;
import druidelf.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 登入
     */
    @Override
    public LoginResponse toLogin(LoginRequest request) {

        String userName = request.getUsername();
        String password = request.getPassword();

        // 执行用户认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password
                )
        );

        // 如果认证成功，生成 JWT
        String jwt = jwtTokenProvider.generateToken(null);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtCode(jwt);
        return loginResponse;
    }
}
