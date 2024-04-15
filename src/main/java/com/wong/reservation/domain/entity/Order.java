package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Wongbuer
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Order implements Serializable {
    /**
     * 订单表主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 员工id
     */
    @TableField(value = "employee_id")
    private Long employeeId;

    /**
     * 订单关闭时间
     */
    @TableField(value = "close_time")
    private LocalDateTime closeTime;

    /**
     * 订单完成时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 订单支付时间
     */
    @TableField(value = "payment_time")
    private LocalDateTime paymentTime;

    /**
     * 订单接受时间
     */
    @TableField(value = "accepted_time")
    private LocalDateTime acceptedTime;

    /**
     * 地址id
     */
    @TableField(value = "address_id")
    private Long addressId;

    /**
     * 实付金额
     */
    @TableField(value = "payment")
    private BigDecimal payment;

    /**
     * 付款类型(先用后付, 先付后用)
     */
    @TableField(value = "payment_type")
    private Integer paymentType;

    /**
     * 服务费(路费, 材料费等)
     */
    @TableField(value = "service_charge")
    private BigDecimal serviceCharge;

    /**
     * 订单状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否已评价
     */
    @TableField(value = "rate")
    private Integer rate;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}