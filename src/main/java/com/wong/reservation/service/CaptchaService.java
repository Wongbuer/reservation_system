package com.wong.reservation.service;

import com.wong.reservation.domain.dto.CaptchaDTO;
import com.wong.reservation.domain.dto.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
public interface CaptchaService {
    Result<Map<String, Object>> generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException;

    boolean verifyCaptcha(HttpServletRequest request, CaptchaDTO code);
}
