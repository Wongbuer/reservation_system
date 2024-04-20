package com.wong.reservation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.wong.reservation.domain.dto.CaptchaDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.service.CaptchaService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
@SaIgnore
@RestController
@RequestMapping(value = "/captcha", method = {RequestMethod.GET, RequestMethod.POST})
public class CaptchaController {
    @Resource
    private CaptchaService captchaService;

    /**
     * 生成验证码
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Result<Map < String, Object>> 图片验证码和其UUID
     * @throws IOException IO异常
     */
    @RequestMapping("/generate")
    public Result<Map<String, Object>> captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return captchaService.generateCaptcha(request, response);
    }

    /**
     * 验证验证码
     *
     * @param request    HttpServletRequest
     * @param captchaDTO 验证码
     * @return boolean 验证结果
     */
    @RequestMapping("/verify")
    public boolean verify(HttpServletRequest request, CaptchaDTO captchaDTO) {
        return captchaService.verifyCaptcha(request, captchaDTO);
    }
}
