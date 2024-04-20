package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Evaluation;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Wongbuer
 * @createDate 2024/4/15
 */
@RestController
@RequestMapping(value = "/order", method = {RequestMethod.GET, RequestMethod.POST})
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 获取订单信息(默认以订单创建时间降序)
     *
     * @param status 订单状态
     * @param sort   是否排序
     * @return Result<List < Order>> 订单列表
     */
    @Operation(summary = "获取订单信息(默认以订单创建时间降序)")
    @RequestMapping
    public Result<List<Order>> getOrder(String status, Boolean sort) {
        return orderService.getOrderByUserId(status, sort);
    }

    /**
     * 添加订单
     *
     * @param order 订单
     * @return Result<?> 添加结果
     */
    @Operation(summary = "添加订单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<?> createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    /**
     * 接受订单
     *
     * @param id 订单ID
     * @return Result<?> 接受订单结果
     */
    @Operation(summary = "接受订单")
    @RequestMapping(value = "/accept", method = RequestMethod.GET)
    public Result<?> acceptOrder(Long id) {
        return orderService.acceptOrder(id);
    }

    /**
     * 支付订单
     *
     * @param id 订单ID
     */
    @Operation(summary = "支付订单")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public void payOrder(Long id, HttpServletResponse response) {
        orderService.payOrder(id, response);
    }

    /**
     * 显示二维码
     *
     * @param id 订单id
     * @param response httpServletResponse
     */
    @Operation(summary = "显示二维码")
    @RequestMapping(value = "/showQrCode", method = RequestMethod.GET)
    public void showQrCode(Long id, HttpServletResponse response) {
        orderService.showQrCode(id, response);
    }

    /**
     * 解析二维码
     *
     * @param content 二维码正文
     * @return Result<?> 解析二维码之后的处理结果
     */
    @Operation(summary = "解析二维码")
    @RequestMapping(value = "/parseQrCode", method = RequestMethod.GET)
    public Result<?> parseQrCode(String content) {
        return orderService.parseQrCode(content);
    }

    /**
     * 评价订单
     *
     * @param evaluation 订单评价
     * @return Result<?> 评价结果
     */
    @Operation(summary = "评价订单")
    @RequestMapping(value = "/evaluate", method = RequestMethod.POST)
    public Result<?> evaluateOrder(@RequestBody Evaluation evaluation) {
        return orderService.evaluateOrder(evaluation);
    }

    /**
     * 修改订单
     *
     * @param order 订单
     * @return Result<?> 修改结果
     */
    @Operation(summary = "修改订单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<?> updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return Result<?> 删除结果
     */
    @Operation(summary = "删除订单")
    @RequestMapping(value = "/delete")
    public Result<?> deleteOrder(Long id) {
        return orderService.deleteOrder(id);
    }


}
