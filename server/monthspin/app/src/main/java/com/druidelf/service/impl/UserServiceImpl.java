package com.druidelf.service.impl;


import cn.hutool.core.util.StrUtil;
import com.druidelf.request.LoginRequest;
import com.druidelf.request.RegisterRequest;
import com.druidelf.response.LoginResponse;
import com.druidelf.service.UserService;
import druidelf.enums.ResponseDataEnums;
import druidelf.exception.DruidElfHitException;
import druidelf.model.ResponseData;
import druidelf.security.JwtTokenProvider;
import com.druidelf.service.wechat.WeChatService;
import com.druidelf.service.wechat.vo.WeChatLoginRequest;
import com.druidelf.service.wechat.vo.WeChatLoginResponse;
import druidelf.bean.spin.SpinUser;
import druidelf.repository.spin.SpinUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final WeChatService weChatService;
    private final SpinUserRepository spinUserRepository;

    /**
     * 登入
     */
    @Override
    public ResponseData<LoginResponse> toLogin(LoginRequest request) {

        String username = null;
        // 执行用户认证(账号密码登入)
        if ( StrUtil.isNotBlank(request.getUsername()) && StrUtil.isNotBlank(request.getPassword()) ) {
            Authentication authentication
                    = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }


        //  微信小程序登入
        if (StrUtil.isNotBlank( request.getWeChatCode()) ) {
            //  获取用户微信端信息
            WeChatLoginRequest weChatLoginRequest = new WeChatLoginRequest();
            weChatLoginRequest.setJsCode(request.getWeChatCode());
            WeChatLoginResponse weChatLoginResponse = weChatService.code2Session(weChatLoginRequest);
            if ( weChatLoginResponse.getResult() != WeChatLoginResponse.WeChatErrorCode.SUCCESS ) throw new DruidElfHitException("登入失败");
            //  查询微信是否绑定用户
            SpinUser user = spinUserRepository.findSpinUserByOpenId(weChatLoginResponse.getOpenid());
            if ( user == null ) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setOpenId(weChatLoginResponse.getOpenid());
                ResponseData<LoginResponse> failure = ResponseData.FAILURE(loginResponse);
                failure.setStatusCode(ResponseDataEnums.RESPONSE_FAIL_NOT_BIND_WECHAT.getStatusCode());
                failure.setMessage(ResponseDataEnums.RESPONSE_FAIL_NOT_BIND_WECHAT.getName());
                return failure;
            }
            username = user.getUsername();
        }

        // 如果认证成功，生成 JWT
        if ( username == null ) {
            throw new DruidElfHitException("登入失败");
        }

        String jwt = jwtTokenProvider.generateToken(username);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtCode(jwt);
        return ResponseData.SUCCESS(loginResponse);
    }

    /**
     * 注册
     */
    @Override
    public void register(RegisterRequest request) {

        if ( StrUtil.isBlank(request.getUsername()) ) {
            request.setUsername(UUID.randomUUID().getLeastSignificantBits()+"");
        }

        SpinUser spinUser = new SpinUser();
        spinUser
                .setUsername(request.getUsername())
                .setAddTime(LocalDateTime.now())
                .setPassword(UUID.randomUUID().toString())
                .setPhone(request.getPhone())
                .setRealName(request.getRealName())
                .setOpenId(request.getOpenId())
                .setStatus(false)
                ;
        spinUserRepository.save(spinUser);

    }
}
