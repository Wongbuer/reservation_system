package com.wong.reservation.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.wong.reservation.domain.dto.AliPayDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public String pay(AliPayDTO aliPayDTO) {
//        AlipayTradePagePayResponse response;
        AlipayTradeWapPayResponse response;
        try {
            //  发起API调用（以创建当面付收款二维码为例）
//            response = Factory.Payment.Page()
//                    .pay(URLEncoder.encode(aliPayDTO.getSubject(), StandardCharsets.UTF_8), aliPayDTO.getTraceNo(), String.valueOf(aliPayDTO.getTotalAmount()), "http://localhost:8080/alipay/success");
            response = Factory.Payment.Wap()
                    .pay(URLEncoder.encode(aliPayDTO.getSubject(), StandardCharsets.UTF_8), aliPayDTO.getTraceNo(), String.valueOf(aliPayDTO.getTotalAmount()), "", "http://localhost:8080/alipay/success");
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.getBody();
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");
            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
                System.out.println(params);
                // 更新订单未已支付
                System.out.println("更新订单状态");
            }
        }
        return "success";
    }

    @RequestMapping("/success")
    @ResponseBody
    public String success() {
        return "success";
    }
}

