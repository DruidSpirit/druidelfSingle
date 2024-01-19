package com.druidelf.response;

import lombok.Data;

@Data
public class LoginResponse {

    private String openId;
    private String jwtCode;
}
