package com.wong.reservation.service;

import com.wong.reservation.domain.dto.LoginDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.dto.SignUpDTO;
import com.wong.reservation.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.request.AuthRequest;

public interface LoginService {
    /**
     * 用户注册
     *
     * @param signUpDTO 用户注册信息
     * @return User 用户信息
     */
    Result<User> userSignUp(SignUpDTO signUpDTO);

    /**
     * 用户登录
     *
     * @param loginDTO 用户登录信息
     * @return 用户登录凭证
     */
    Result<Object> userLogin(HttpServletRequest request, LoginDTO loginDTO);

    AuthRequest getAuthRequest(String source);
}
