package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Wongbuer
 * @TableName employee_service
 */
@TableName(value = "employee_service")
@Data
public class EmployeeService implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 员工服务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
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
     * 服务评分
     */
    @TableField(value = "rate")
    private Integer rate;
    /**
     * 员工服务单价
     */
    @TableField(value = "price")
    private BigDecimal price;
    /**
     * 员工服务描述
     */
    @TableField(value = "description")
    private String description;
    /**
     * 员工服务折扣
     */
     @TableField(value = "discount")
    private BigDecimal discount;
    /**
     * 员工服务计价单位
     */
    @TableField(value = "time_unit")
    private String timeUnit;
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
     * 员工服务详细介绍图片
     */
    @TableField(value = "detailed_pictures")
    private String detailedPictures;
}