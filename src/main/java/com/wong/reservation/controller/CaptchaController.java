package com.wong.reservation.controller;

import com.wong.reservation.service.CaptchaService;
import io.springboot.captcha.utils.CaptchaJakartaUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
@RestController
@RequestMapping(value = "/captcha", method = {RequestMethod.GET, RequestMethod.POST})
public class CaptchaController {
    @Resource
    private CaptchaService captchaService;

    @RequestMapping("/generate")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        captchaService.generateCaptcha(request, response);
    }

    @RequestMapping("/verify")
    public boolean verify(HttpServletRequest request, String code) {
        return captchaService.verifyCaptcha(request, code);
    }
}
