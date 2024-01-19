package com.druidelf.controller;

import com.druidelf.request.LoginRequest;
import com.druidelf.request.RegisterRequest;
import com.druidelf.response.LoginResponse;
import com.druidelf.service.UserService;
import druidelf.bean.spin.SpinUser;
import druidelf.enums.ResponseDataEnums;
import druidelf.model.ResponseData;
import druidelf.repository.spin.SpinUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final SpinUserRepository spinUserRepository;
    private final UserService userService;

    /**
     * 登入
     */
    @PostMapping(value = "login")
    public ResponseData<LoginResponse> login(@RequestBody LoginRequest request ) {
        return userService.toLogin(request);
    }

    /**
     * 注册
     */
    @PostMapping(value = "register")
    public ResponseData<Object> register(@RequestBody @Validated RegisterRequest request ) {

        userService.register(request);
        return ResponseData.SUCCESS(ResponseDataEnums.RESPONSE_SUCCESS_REGISTER);
    }

    /**
     * 获取当前登入用户信息
     */
    @GetMapping(value = "getUserInfo")
    public ResponseData<SpinUser>  getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        SpinUser spinUserByUsername = spinUserRepository.findSpinUserByUsername(username);
        return ResponseData.SUCCESS(spinUserByUsername);
    }

}
