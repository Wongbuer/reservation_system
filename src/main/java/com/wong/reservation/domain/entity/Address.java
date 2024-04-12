package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Wongbuer
 * @TableName address
 */
@TableName(value = "address")
@Data
public class Address implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 地址表主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * "收货人"名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * "收货人"电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 地址省份
     */
    @TableField(value = "province")
    private String province;
    /**
     * 地址城市
     */
    @TableField(value = "city")
    private String city;
    /**
     * 地址区县
     */
    @TableField(value = "district")
    private String district;
    /**
     * 详细地址
     */
    @TableField(value = "full_address")
    private String fullAddress;
    /**
     * 邮政编码
     */
    @TableField(value = "postal_code")
    private String postalCode;
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
}