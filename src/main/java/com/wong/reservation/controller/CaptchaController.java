package com.wong.reservation.controller;

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
@RestController
@RequestMapping(value = "/captcha", method = {RequestMethod.GET, RequestMethod.POST})
public class CaptchaController {
    @Resource
    private CaptchaService captchaService;

    @RequestMapping("/generate")
    public Result<Map<String, Object>> captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return captchaService.generateCaptcha(request, response);
    }

    @RequestMapping("/verify")
    public boolean verify(HttpServletRequest request, CaptchaDTO captchaDTO) {
        return captchaService.verifyCaptcha(request, captchaDTO);
    }
}
