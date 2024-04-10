package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.SocialUser;
import com.wong.reservation.service.LoginService;
import com.wong.reservation.service.SocialUserService;
import jakarta.annotation.Resource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wongbuer
 */
@RestController
@RequestMapping(value = "/oauth/callback", method = {RequestMethod.GET, RequestMethod.POST})
public class OAuthController {
    @Resource
    private LoginService loginService;
    @Resource
    private SocialUserService socialUserService;

    /**
     * github登录成功回调
     *
     * @param callback 授权回调时的参数类
     * @return 登录结果
     */
    @RequestMapping("/github")
    public Result<Object> login(AuthCallback callback) {
        AuthRequest authRequest = loginService.getAuthRequest("github");
        AuthResponse authResponse = authRequest.login(callback);
        if (authResponse.getCode() == 2000) {
            try {
                AuthUser authUser = (AuthUser) authResponse.getData();
                String uuid = authUser.getUuid();
                String accessToken = authUser.getToken().getAccessToken();
                SocialUser socialUser = new SocialUser(uuid, "github", accessToken);
                socialUserService.save(socialUser);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.fail("登录失败");
            }
        }
        // TODO: 添加自定义callback处理
        return Result.success(authResponse);
    }
}
