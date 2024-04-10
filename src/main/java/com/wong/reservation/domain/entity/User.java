package com.wong.reservation.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
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
    @TableId(value = "id")
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
}