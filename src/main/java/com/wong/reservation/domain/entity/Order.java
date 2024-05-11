package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Wongbuer
 * @TableName orders
 */
@TableName(value = "orders")
@Data
public class Order implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 订单表主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
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
     * 服务id
     */
    @TableField(value = "service_id")
    private Long serviceId;
    /**
     * 订单预约时间
     */
    @TableField(value = "reservation_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationTime;
    /**
     * 订单关闭时间
     */
    @TableField(value = "close_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeTime;
    /**
     * 订单完成时间
     */
    @TableField(value = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /**
     * 订单支付时间
     */
    @TableField(value = "payment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;
    /**
     * 订单接受时间
     */
    @TableField(value = "accepted_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 服务的时间单位数
     */
    @TableField(value = "unit_count")
    private Integer unitCount;
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
     * 是否已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    /**
     * 员工服务表主键
     */
    @TableField(exist = false)
    private Long employeeServiceId;
}