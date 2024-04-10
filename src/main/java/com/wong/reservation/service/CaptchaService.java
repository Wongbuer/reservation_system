package com.wong.reservation.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
public interface CaptchaService {
    void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException;
    boolean verifyCaptcha(HttpServletRequest request, String code);
}
