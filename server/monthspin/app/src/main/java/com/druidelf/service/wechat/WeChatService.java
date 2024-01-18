package com.druidelf.service.wechat;

import com.druidelf.config.WeChatConfigProperty;
import com.druidelf.service.wechat.vo.WeChatLoginRequest;
import com.druidelf.service.wechat.vo.WeChatLoginResponse;
import druidelf.exception.DruidElfHitException;
import druidelf.util.UtilForData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@RequiredArgsConstructor
public class WeChatService {

    private final RestTemplate restTemplate;
    private final WeChatConfigProperty property;

    /**
     * 微信小程序登录接口
     *
     * @param request 登录请求参数
     * @return 登录响应结果
     */
    public WeChatLoginResponse code2Session(WeChatLoginRequest request) {
        // 构建请求URL
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + property.getAppId() +
                "&secret=" + property.getAppSecret() +
                "&js_code=" + request.getJsCode() +
                "&grant_type=authorization_code";

        // 发送 GET 请求并获取响应
        log.info("发送到微信code请求接口参数是{}",request);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String body = responseEntity.getBody();
        log.info("接收微信code请求接口响应是{}",body);

        WeChatLoginResponse weChatLoginResponse = UtilForData.transJsonStringToT(body, WeChatLoginResponse.class);

        // 返回响应体
        return weChatLoginResponse;
    }

}
