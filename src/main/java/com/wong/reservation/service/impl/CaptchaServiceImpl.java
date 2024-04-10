package com.wong.reservation.service.impl;

import com.wong.reservation.service.CaptchaService;
import io.springboot.captcha.utils.CaptchaJakartaUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Override
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaJakartaUtil.out(request, response);
    }

    @Override
    public boolean verifyCaptcha(HttpServletRequest request, String code) {
        return CaptchaJakartaUtil.ver(code, request);
    }
}
