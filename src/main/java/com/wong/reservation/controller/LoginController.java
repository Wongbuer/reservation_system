package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.LoginDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.dto.SignUpDTO;
import com.wong.reservation.domain.entity.User;
import com.wong.reservation.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Wongbuer
 */
@RestController
@RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
public class LoginController {
    @Resource
    private LoginService loginService;

    /**
     * 用户注册
     *
     * @param signUpDTO 用户注册信息包装类
     * @return 用户实体
     */
    @Operation(summary = "用户注册")
    @RequestMapping("/signup")
    public Result<User> userSignUp(@RequestBody SignUpDTO signUpDTO) {
        return loginService.userSignUp(signUpDTO);
    }

    /**
     * 用户登录
     *
     * @param loginDTO 用户登录信息包装类
     * @return 用户信息
     */
    @Operation(summary = "用户登录")
    @RequestMapping
    public Result<Object> userLogin(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return loginService.userLogin(request, loginDTO);
    }

    /**
     * 测试登录
     *
     * @return 成功信息
     */
    @Operation(summary = "测试登录")
    @RequestMapping("/test")
    public Result<?> test() {
        return Result.success("test login");
    }

    /**
     * 第三方登录
     *
     * @param source   第三方登录源
     * @param response http相应
     * @throws IOException IO异常
     */
    @RequestMapping("/thirdPartyLogin/{source}")
    public void thirdPartyLogin(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = loginService.getAuthRequest(source);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }
}
