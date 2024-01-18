package com.druidelf.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {

    private String username;

    /**
     * 真实名称
     */
    @NotBlank
    private String realName;

    /**
     * 手机号码
     */
    private String phone;
}
