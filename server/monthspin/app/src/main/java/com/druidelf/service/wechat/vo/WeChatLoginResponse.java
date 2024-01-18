package com.druidelf.service.wechat.vo;

import druidelf.util.UtilForData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class WeChatLoginResponse {

    private String openid;    // 用户唯一标识
    private String session_key;// 会话密钥
    private String unionid;    // 用户在开放平台的唯一标识符
    private int errcode;       // 错误码
    private String errmsg;     // 错误信息

    public WeChatErrorCode getResult(){
        WeChatErrorCode enumByCode = UtilForData.getEnumByCode(WeChatErrorCode.values(), WeChatErrorCode::getErrorCode, this.errcode);
        if ( enumByCode == null ) {
            return WeChatErrorCode.UNKNOWN;
        }
        return enumByCode;
    }

    @Getter
    @AllArgsConstructor
    public enum WeChatErrorCode {
        UNKNOWN(-2, "未知异常", "无"),
        SUCCESS(0, "成功", "无"),
        CODE_INVALID(40029, "code 无效", "js_code 无效"),
        API_LIMIT_REACHED(45011, "api minute-quota reach limit", "API 调用太频繁，请稍候再试"),
        CODE_BLOCKED(40226, "code blocked", "高风险等级用户，小程序登录拦截。风险等级详见用户安全解方案"),
        SYSTEM_ERROR(-1, "system error", "系统繁忙，此时请开发者稍候再试")
        ;

        private final int errorCode;
        private final String errorCodeValue;
        private final String solution;
    }

}
