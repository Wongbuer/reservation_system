package com.wong.reservation.service.impl;

import com.wong.reservation.domain.dto.CaptchaDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.service.CaptchaService;
import com.wong.reservation.utils.RedisUtils;
import io.springboot.captcha.SpecCaptcha;
import io.springboot.captcha.utils.CaptchaJakartaUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.wong.reservation.constant.RedisConstant.CAPTCHA_PREFIX;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Resource
    private RedisUtils redisUtils;

    @Override
    public Result<Map<String, Object>> generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 根据cookie-session处理对应验证码
//        return generateCaptchaWithSession(request, response);
        // 根据redis处理对应验证码
        return generateCaptchaWithRedis(request, response);
    }

    @Override
    public boolean verifyCaptcha(HttpServletRequest request, CaptchaDTO captchaDTO) {
        // 根据cookie-session校验对应验证码
//        return verifyCaptchaWithSession(request, captchaDTO.getCode());
        // 根据redis校验对应验证码
        return verifyCaptchaWithRedis(request, captchaDTO);
    }

    private boolean verifyCaptchaWithSession(HttpServletRequest request, String code) {
        return CaptchaJakartaUtil.ver(code, request);
    }

    private Result<Map<String, Object>> generateCaptchaWithSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaJakartaUtil.out(request, response);
        return null;
    }

    private Result<Map<String, Object>> generateCaptchaWithRedis(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("image", specCaptcha.toBase64());
        // 修改redis中验证码储存方式为hash
        redisUtils.hset(CAPTCHA_PREFIX, key, verCode, 60 * 30);
        return Result.success(result);
    }

    private boolean verifyCaptchaWithRedis(HttpServletRequest request, CaptchaDTO captchaDTO) {
        String redisCode = (String) redisUtils.hget(CAPTCHA_PREFIX, captchaDTO.getUuid());
        if (redisCode == null) {
            return false;
        }
        boolean result = redisCode.equals(captchaDTO.getCode());
        if (result) {
            // 修改redis中验证码储存方式为hash
            redisUtils.hdel(CAPTCHA_PREFIX, captchaDTO.getUuid());
        }
        return result;
    }
}
