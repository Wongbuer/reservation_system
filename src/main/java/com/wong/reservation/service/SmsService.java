package com.wong.reservation.service;

import com.wong.reservation.domain.dto.CaptchaDTO;
import com.wong.reservation.domain.dto.Result;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
public interface SmsService {
    /**
     * 发送短信验证码
     *
     * @param phone      手机号
     * @param captchaDTO 验证码包装类
     * @return 发送结果
     */
    Result<String> sendMessage(String phone, CaptchaDTO captchaDTO);
}
