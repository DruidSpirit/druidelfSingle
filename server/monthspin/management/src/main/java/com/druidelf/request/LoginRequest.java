package com.druidelf.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;

    private String weChatCode;
}
