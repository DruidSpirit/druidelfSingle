package com.druidelf.service;


import com.druidelf.request.LoginRequest;
import com.druidelf.request.RegisterRequest;
import com.druidelf.response.LoginResponse;

public interface UserService {

    /**
     * 登入
     */
    LoginResponse toLogin(LoginRequest request );

    /**
     * 注册
     */
    void register( RegisterRequest request );
}
