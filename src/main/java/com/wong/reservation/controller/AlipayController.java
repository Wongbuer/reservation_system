package com.wong.reservation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.service.AliPayService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Wongbuer
 */
@SaIgnore
@Controller
@RequestMapping("/alipay")
public class AlipayController {
    @Resource
    private AliPayService aliPayService;

    @RequestMapping("/exit")
    @ResponseBody
    public Result<?> exit() {
        return Result.fail("支付退出");
    }

    /**
     * 支付成功后回调
     *
     * @param request request
     * @return Result 支付结果
     */
    @RequestMapping(value = "/afterPay", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> success(HttpServletRequest request) {
        return aliPayService.afterPay(request);
    }
}

