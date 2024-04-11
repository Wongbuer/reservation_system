package com.wong.reservation.service.impl;

import cn.hutool.core.util.PhoneUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.wong.reservation.domain.dto.CaptchaDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.service.CaptchaService;
import com.wong.reservation.service.SmsService;
import com.wong.reservation.utils.RedisUtils;
import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.wong.reservation.constant.RedisConstant.SMS_LOGIN_PREFIX;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
@Service
public class AlibabaSmsServiceImpl implements SmsService {
    private AsyncClient client;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private CaptchaService captchaService;

    // 初始化阿里云短信客户端
//    @PostConstruct
    public void clientInit() {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(System.getProperty("ALIBABA_CLOUD_ACCESS_KEY_ID"))
                .accessKeySecret(System.getProperty("ALIBABA_CLOUD_ACCESS_KEY_SECRET"))
                .build());
        client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
    }

    @SneakyThrows
    @Override
    public Result<String> sendMessage(String phone, CaptchaDTO captchaDTO) {
        // 判断手机号是否正确
        if (!PhoneUtil.isMobile(phone)) {
            return Result.fail(400, "手机号格式错误");
        }
        // 判断图片验证码是否正确
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (!captchaService.verifyCaptcha(request, captchaDTO)) {
            return Result.fail(400, "图片验证码错误");
        }
        // 随机生成四位数字验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        // 阿里云发送短信
//        sendSms(phone, code);
        // redis缓存验证码, 五分钟后过期
        redisUtils.set(SMS_LOGIN_PREFIX + phone, code, 60 * 5);
        return Result.success("发送成功");
    }

    private void sendSms(String phone, String code) {
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("wongbuer")
                .templateCode("SMS_268585414")
                .phoneNumbers(phone)
                .templateParam("{\"code\":\"" + code + "\"}")
                .build();
        client.sendSms(sendSmsRequest);
    }
}
