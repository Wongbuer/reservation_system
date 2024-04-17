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
 * @TableName service
 */
@TableName(value = "service")
@Data
public class Service implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 服务id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 服务名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 服务介绍
     */
    @TableField(value = "description")
    private String description;
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