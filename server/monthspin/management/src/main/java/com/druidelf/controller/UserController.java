package com.druidelf.controller;

import com.druidelf.request.LoginRequest;
import com.druidelf.response.LoginResponse;
import com.druidelf.service.UserService;
import druidelf.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "login")
    public ResponseData<LoginResponse> login(@RequestBody LoginRequest request ) {

        LoginResponse loginResponse = userService.toLogin(request);
        return ResponseData.SUCCESS(loginResponse);
    }

}
