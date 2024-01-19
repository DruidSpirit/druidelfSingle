package com.druidelf.service;


import com.druidelf.request.LoginRequest;
import com.druidelf.request.RegisterRequest;
import com.druidelf.response.LoginResponse;
import druidelf.model.ResponseData;

public interface UserService {

    /**
     * 登入
     */
    ResponseData<LoginResponse> toLogin(LoginRequest request );

    /**
     * 注册
     */
    void register( RegisterRequest request );
}
