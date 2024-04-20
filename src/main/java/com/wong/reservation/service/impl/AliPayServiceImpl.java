package com.wong.reservation.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wong.reservation.constant.OrderStatusConstant;
import com.wong.reservation.domain.dto.AliPayDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.mapper.OrderMapper;
import com.wong.reservation.service.AliPayService;
import com.wong.reservation.utils.lock.OrderLock;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.wong.reservation.constant.SystemConstant.ORDER_LOCK_PAY_PREFIX;

/**
 * @author Wongbuer
 * @createDate 2024/4/20
 */
@Service
public class AliPayServiceImpl implements AliPayService {
    @Resource
    private OrderLock orderLock;
    @Resource
    private OrderMapper orderMapper;
    @Override
    public void goToPay(AliPayDTO aliPayDTO, HttpServletResponse httpServletResponse) throws Exception {
        //        AlipayTradePagePayResponse response;
        // TODO: 根据用户访问设备类型，选择不同支付页面
        AlipayTradeWapPayResponse response;

        String encodedParam = URLEncoder.encode(aliPayDTO.getSubject(), StandardCharsets.UTF_8);
        String exitUrl = "http://localhost:8080/alipay/exit";
        String returnUrl = "http://localhost:8080/alipay/afterPay";

        //  发起API调用（以创建当面付收款二维码为例）
//        response = Factory.Payment.Page()
//                .pay(URLEncoder.encode(aliPayDTO.getSubject(), StandardCharsets.UTF_8), aliPayDTO.getTraceNo(), String.valueOf(aliPayDTO.getTotalAmount()), "http://localhost:8080/alipay/success");

        response = Factory.Payment.Wap()
                .pay(encodedParam, aliPayDTO.getTraceNo(), String.valueOf(aliPayDTO.getTotalAmount()), exitUrl, returnUrl);
        httpServletResponse.setHeader("Content-Type", "text/html;charset=utf-8");
        httpServletResponse.getWriter().write(response.body);
    }

    @Override
    public Result<?> afterPay(HttpServletRequest request) {
        // 通过request.getParameterMap()获取支付宝返回的参数
        Map<String, String> resultMap = new HashMap<>(request.getParameterMap().size());
        request.getParameterMap().forEach((k, v) -> resultMap.put(k, v[0]));
        // 上锁
        String lockKey = ORDER_LOCK_PAY_PREFIX + resultMap.get("out_trade_no");
        boolean isLocked = orderLock.acquireLock(lockKey, 5000);
        if (isLocked) {
            // 查询金额是否正确
            try {
                TimeUnit.SECONDS.sleep(15);
                LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
                wrapper
                        .eq(Order::getStatus, OrderStatusConstant.ACCEPTED)
                        .eq(Order::getPayment, resultMap.get("total_amount"))
                        .eq(Order::getId, resultMap.get("out_trade_no"));
                Order order = orderMapper.selectOne(wrapper);
                if (ObjectUtils.isEmpty(order)) {
                    // TODO: 异常订单处理(退款)
                    return Result.fail("支付金额有误");
                }
                // 写入支付信息
                LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper
                        .eq(Order::getId, order.getId())
                        .set(Order::getPaymentTime, resultMap.get("timestamp"))
                        .set(Order::getStatus, OrderStatusConstant.PAID);
                orderMapper.update(order, updateWrapper);
                return Result.success("支付成功");
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: 异常订单处理(退款)
                return Result.fail("支付失败");
            } finally {
                // 释放锁
                orderLock.releaseLock(lockKey);
            }
        } else {
            return Result.fail("支付超时");
        }
    }
}
