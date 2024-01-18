package com.druidelf.service.wechat.vo;

import lombok.Data;

@Data
public class WeChatLoginRequest {

    private String jsCode;    // 登录时获取的 code，可通过 wx.login 获取
}
