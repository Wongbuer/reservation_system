package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Wongbuer
 * @TableName user
 */
@TableName(value = "user")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;
    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;
    /**
     * 性别
     */
    @TableField(value = "gender")
    private String gender;
    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 默认地址id
     */
    @TableField(value = "default_address_id")
    private Long defaultAddressId;
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
}