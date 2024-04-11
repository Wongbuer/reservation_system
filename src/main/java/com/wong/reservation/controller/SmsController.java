package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.CaptchaDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
@RestController
@RequestMapping(value = "/sms", method = {RequestMethod.GET, RequestMethod.POST})
public class SmsController {
    @Resource
    private SmsService smsService;

    /**
     * 发送短信
     *
     * @param phone 手机号
     * @return Result<java.lang.String>
     */
    @Operation(summary = "发送短信")
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    public Result<String> sendSmsMessage(String phone, CaptchaDTO captchaDTO) {
        return smsService.sendMessage(phone, captchaDTO);
    }

}
