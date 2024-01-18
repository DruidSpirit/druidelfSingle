package com.druidelf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatConfigProperty {

    //  微信-应用ID
    private String appId;

    //  小程序密钥
    private String appSecret;

}
